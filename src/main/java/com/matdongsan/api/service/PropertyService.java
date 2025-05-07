package com.matdongsan.api.service;

import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyDeleteRequest;
import com.matdongsan.api.dto.property.PropertyGetRequest;
import com.matdongsan.api.dto.property.PropertyUpdateRequest;
import com.matdongsan.api.mapper.PropertyDetailMapper;
import com.matdongsan.api.mapper.PropertyImagesMapper;
import com.matdongsan.api.mapper.PropertyMapper;
import com.matdongsan.api.vo.PropertyDetailVO;
import com.matdongsan.api.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: optional 추가, s3 추가, exception 전역처리, 성능 추가 점검, 사용자 관련 체크

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PropertyService {

  private final PropertyMapper mapper;

  private final PropertyDetailMapper detailMapper;

  private final PropertyImagesMapper imagesMapper;

  public Long createProperty(PropertyCreateRequest request) {
    //property 생성
    Long propertyId = mapper.insertProperty(request);
    if (propertyId == null) throw new RuntimeException("매물 저장 실패");
    
    //property_detail 생성
    // s3를 통해 이미지 url 추출 로직 추가 해야함
    request.getDetail().setPropertyId(propertyId);
    int detailResult = detailMapper.insertPropertyDetail(request.getDetail());
    if (detailResult != 1) throw new RuntimeException("상세 저장 실패");

    //property_images 생성
    if (request.getImages() != null) {
      for (String imageUrl : request.getImageUrls()) {
        int imageResult = imagesMapper.insertPropertyImage(propertyId, imageUrl);
        if (imageResult != 1) throw new RuntimeException("이미지 저장 실패: " + imageUrl);
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

}
