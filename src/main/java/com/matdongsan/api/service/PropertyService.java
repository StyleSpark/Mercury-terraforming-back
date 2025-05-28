package com.matdongsan.api.service;

import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyDeleteRequest;
import com.matdongsan.api.dto.property.PropertyGetRequest;
import com.matdongsan.api.dto.property.PropertyUpdateRequest;
import com.matdongsan.api.mapper.PaymentMapper;
import com.matdongsan.api.mapper.PropertyDetailMapper;
import com.matdongsan.api.mapper.PropertyImagesMapper;
import com.matdongsan.api.mapper.PropertyMapper;
import com.matdongsan.api.vo.PropertyDetailVO;
import com.matdongsan.api.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: optional 추가, s3 추가, exception 전역처리, 성능 추가 점검, 사용자 관련 체크

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PropertyService {

  private final PropertyMapper mapper;

  private final PropertyDetailMapper detailMapper;

  private final PropertyImagesMapper imagesMapper;

  private final PaymentMapper paymentMapper;

  private final S3Service s3Service;

  private final ImageConversionService imageConversionService;

  public Long createProperty(PropertyCreateRequest request) {

    // 0. 등록권 체크
    // 아래 코드는 기능적으로는 문제가 없지만 동시성 문제에서 취약한 문제를 가지고 있음
    //boolean hasTicket = paymentMapper.checkTicketByUserId(request.getUserId());

    int updatedRows = paymentMapper.consumeTicket(request.getUserId());

    if (updatedRows != 1) {
      throw new IllegalStateException ("등록권이 없습니다.");
    }

    //  1. 썸네일 이미지 업로드 먼저 수행 (ID 없음 → temp 이름)
    if (request.getThumbnail() != null && !request.getThumbnail().isEmpty()) {
      try {
        byte[] webpThumbnail = imageConversionService.convertToWebP(request.getThumbnail());
        String thumbKey = "properties/temp_" + System.currentTimeMillis() + "_thumb.webp";
        String thumbnailUrl = s3Service.uploadBytes(thumbKey, webpThumbnail, "image/webp");
        request.setThumbnailUrl(thumbnailUrl);
      } catch (Exception e) {
        throw new RuntimeException("썸네일 업로드 실패", e);
      }
    }

    // 2. DB에 매물 기본 정보 + 썸네일 URL 한 번에 insert
    mapper.insertProperty(request);
    Long propertyId = request.getId();
    if (propertyId == null) throw new RuntimeException("매물 저장 실패");

    // 3. 상세 정보 저장
    request.getDetail().setPropertyId(propertyId);
    int detailResult = detailMapper.insertPropertyDetail(request.getDetail());
    if (detailResult != 1) throw new RuntimeException("상세 저장 실패");

    // 4. 추가 이미지들 S3 업로드 및 DB 저장
    if (request.getImages() != null && !request.getImages().isEmpty()) {
      for (MultipartFile image : request.getImages()) {
        try {
          byte[] webpImage = imageConversionService.convertToWebP(image);
          String imageKey = "properties/" + propertyId + "_" + System.currentTimeMillis() + ".webp";
          String imageUrl = s3Service.uploadBytes(imageKey, webpImage, "image/webp");

          int result = imagesMapper.insertPropertyImage(propertyId, imageUrl);
          if (result != 1) throw new RuntimeException("이미지 저장 실패: " + imageUrl);

          request.getImageUrls().add(imageUrl); // 리턴용
        } catch (Exception e) {
          throw new RuntimeException("이미지 업로드 실패", e);
        }
      }
    }

    return propertyId;
  }


  @Transactional(readOnly = true)
  public List<PropertyVO> getProperties(PropertyGetRequest request) {
    return mapper.selectProperties(request);
  }

  @Transactional(readOnly = true)
  public PropertyVO getPropertyDetail(Long id) {
    PropertyVO property = mapper.selectPropertyById(id);
    PropertyDetailVO detail = detailMapper.selectPropertyDetailByPropertyId(id);
    List<String> imageUrls = imagesMapper.selectImageUrlsByPropertyId(id);

    property.setDetail(detail);
    property.setImageUrls(imageUrls);

    return property;
  }

  public boolean deleteProperty(PropertyDeleteRequest request) {
    // 매물 삭제
    int deleted = mapper.softDeleteProperty(request);

    /* 상세 정보 삭제 */
    int detailDeleted = detailMapper.softDeletePropertyDetail(request);

    // 이미지 삭제
    int imageDeleted = imagesMapper.softDeletePropertyImages(request);

    return deleted > 0 && detailDeleted >= 0 && imageDeleted >= 0;
  }

  public PropertyVO updateProperty(PropertyUpdateRequest request) {
    Long propertyId = request.getId();

    // 1. property 업데이트
    int updated = mapper.updateProperty(request);
    if (updated != 1) throw new RuntimeException("매물 업데이트 실패");

    // 2. property_detail 업데이트
    request.getDetail().setPropertyId(propertyId);
    int detailUpdated = detailMapper.updatePropertyDetail(request.getDetail());
    if (detailUpdated != 1) throw new RuntimeException("상세 정보 업데이트 실패");

    // 3. 기존 이미지 soft delete
//    int imageDeleted = imagesMapper.softDeletePropertyImagesByPropertyId(propertyId);
//    if (imageDeleted < 0) throw new RuntimeException("기존 이미지 삭제 실패");

    // 4. 새 이미지 삽입
    if (request.getImageUrls() != null) {
      for (String imageUrl : request.getImageUrls()) {
        int inserted = imagesMapper.insertPropertyImage(propertyId, imageUrl);
        if (inserted != 1) throw new RuntimeException("새 이미지 삽입 실패: " + imageUrl);
      }
    }

    // 5. 업데이트된 매물 정보 조회 후 리턴
    PropertyVO updatedProperty = mapper.selectPropertyById(propertyId);
    updatedProperty.setDetail(detailMapper.selectPropertyDetailByPropertyId(propertyId));
    updatedProperty.setImageUrls(imagesMapper.selectImageUrlsByPropertyId(propertyId));

    return updatedProperty;
  }

  public Boolean existsFavorite(Long userId, Long propertyId) {
    return detailMapper.existsFavorite(userId,propertyId);
  }

  public Set<Long> getFavoritePropertyIds(Long userId, List<Long> propertyIds) {
    if (propertyIds == null || propertyIds.isEmpty()) return Set.of();
    return new HashSet<>(mapper.getFavoritePropertyIds(userId, propertyIds));
  }
}
