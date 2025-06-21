package com.matdongsan.api.service;

import com.matdongsan.api.dto.property.*;
import com.matdongsan.api.mapper.*;
import com.matdongsan.api.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

  /**
   * a매물 등록
   * @param request
   * @return
   */
  public Long createProperty(PropertyCreateRequest request) {
    consumeTicket(request.getUserId());
    handleThumbnailUpload(request);
    insertPropertyAndDetails(request);
    uploadAndSaveImages(request);
    handleTags(request.getTags(), request.getId());
    return request.getId();
  }

  /**
   * 매물 수정
   * @param request
   * @return
   */
  public PropertyVO updateProperty(PropertyUpdateRequest request) {
    validatePropertyOwnership(request);
    handleThumbnailUpdate(request);
    updatePropertyAndDetails(request);
    handleImageUpdates(request);
    handleTagsUpdate(request);
    return getFullProperty(request.getId());
  }

  /**
   * 매물 삭제
   * @param request
   * @return
   */
  public boolean deleteProperty(PropertyDeleteRequest request) {
    int deleted = mapper.softDeleteProperty(request);
    int detailDeleted = detailMapper.softDeletePropertyDetail(request);
    int imageDeleted = imagesMapper.softDeletePropertyImages(request);
    return deleted > 0 && detailDeleted >= 0 && imageDeleted >= 0;
  }

  /**
   * 매물 조회
   * @param request
   * @return
   */
  @Transactional(readOnly = true)
  public List<PropertyVO> getProperties(PropertyGetRequest request) {
    List<PropertyVO> properties = mapper.selectProperties(request);
    properties.forEach(p -> p.setTags(mapper.getTags(p)));
    return properties;
  }

  /**
   * 매물 상세 조회
   * @param id
   * @return
   */
  @Transactional(readOnly = true)
  public PropertyVO getPropertyDetail(Long id) {
    return getFullProperty(id);
  }

  /**
   * 즐겨찾기 존재 여부
   * @param userId
   * @param propertyId
   * @return
   */
  public Boolean existsFavorite(Long userId, Long propertyId) {
    return detailMapper.existsFavorite(userId, propertyId);
  }

  /**
   * 즐겨찾기 된 매물 id 조회
   * @param userId
   * @param propertyIds
   * @return
   */
  public Set<Long> getFavoritePropertyIds(Long userId, List<Long> propertyIds) {
    return propertyIds == null || propertyIds.isEmpty() ? Set.of() :
            new HashSet<>(mapper.getFavoritePropertyIds(userId, propertyIds));
  }

  /**
   * 지도 범위 안 매물 조회
   * @param request
   * @return
   */
  public List<PropertyVO> getPropertiesWithinBounds(MapBoundsRequestDto request) {
    List<PropertyVO> properties = mapper.selectPropertiesWithinBounds(request);
    properties.forEach(p -> p.setTags(mapper.getTags(p)));
    return properties;
  }

  /**
   * 티켓 사용
   * @param userId
   */
  private void consumeTicket(Long userId) {
    if (paymentMapper.consumeTicket(userId) != 1) {
      throw new IllegalStateException("등록권이 없습니다.");
    }
  }

  /**
   * 썸네일 S3 업로드
   * @param request
   */
  private void handleThumbnailUpload(PropertyCreateRequest request) {
    MultipartFile thumbnail = request.getThumbnail();
    if (thumbnail == null || thumbnail.isEmpty()) return;

    try {
      byte[] data = imageConversionService.convertToWebP(thumbnail);
      String key = "properties/temp_" + System.currentTimeMillis() + "_thumb.webp";

      // DB 커밋 이후에 S3 업로드
      TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
        @Override
        public void afterCommit() {
          String url = s3Service.uploadBytes(key, data, "image/webp");
          request.setThumbnailUrl(url); // 커밋 후 URL 설정
        }
      });

    } catch (Exception e) {
      throw new RuntimeException("썸네일 변환 실패", e);
    }
  }

  /**
   * 매물, 매물 상세 등록
   * @param request
   */
  private void insertPropertyAndDetails(PropertyCreateRequest request) {
    mapper.insertProperty(request);
    Optional.ofNullable(request.getId()).orElseThrow(() -> new RuntimeException("매물 저장 실패"));

    request.getDetail().setPropertyId(request.getId());
    if (detailMapper.insertPropertyDetail(request.getDetail()) != 1) {
      throw new RuntimeException("상세 저장 실패");
    }
  }

  /**
   * 업로드 추가 이미지들
   * @param request
   */
  private void uploadAndSaveImages(PropertyCreateRequest request) {
    List<MultipartFile> images = request.getImages();
    if (images == null || images.isEmpty()) return;

    List<ImageToUpload> imageQueue = new ArrayList<>();

    for (MultipartFile image : images) {
      try {
        byte[] data = imageConversionService.convertToWebP(image);
        String key = "properties/" + request.getId() + "_" + System.currentTimeMillis() + ".webp";

        imageQueue.add(new ImageToUpload(key, data));

      } catch (Exception e) {
        throw new RuntimeException("이미지 변환 실패", e);
      }
    }

    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
      @Override
      public void afterCommit() {
        for (ImageToUpload upload : imageQueue) {
          String url = s3Service.uploadBytes(upload.key, upload.data, "image/webp");
          imagesMapper.insertPropertyImage(request.getId(), url);
          request.getImageUrls().add(url);
        }
      }
    });
  }

  /**
   * 트랜잭션 이후 업로드할 이미지 정보 보관용 내부 클래스
   */
  private static class ImageToUpload {
    String key;
    byte[] data;

    ImageToUpload(String key, byte[] data) {
      this.key = key;
      this.data = data;
    }
  }

  /**
   * 태그 등록 및 매물-태그 매핑
   * @param tagNames
   * @param propertyId
   */
  private void handleTags(List<String> tagNames, Long propertyId) {
    if (tagNames == null || tagNames.isEmpty()) return;

    List<Tag> existingTags = mapper.findTagsByNames(tagNames);
    Set<String> existingNames = existingTags.stream().map(Tag::getName).collect(Collectors.toSet());

    List<String> newTags = tagNames.stream()
            .filter(name -> !existingNames.contains(name))
            .toList();

    if (!newTags.isEmpty()) {
      mapper.insertIgnoreDuplicates(newTags);
    }

    List<Tag> allTags = mapper.findTagsByNames(tagNames);
    List<Map<String, Long>> mappings = allTags.stream()
            .map(tag -> {
              Map<String, Long> map = new HashMap<>();
              map.put("propertyId", propertyId);
              map.put("tagId", tag.getId());
              return map;
            })
            .collect(Collectors.toList());

    mapper.bulkInsert(mappings);
  }

  /**
   * 매물 수정 전 본인 소유 여부 확인
   * @param request
   */
  private void validatePropertyOwnership(PropertyUpdateRequest request) {
    if (!mapper.checkPropertyByUserId(request)) {
      throw new SecurityException("본인의 매물만 수정 가능합니다.");
    }
  }

  /**
   * 썸네일 수정 처리
   * @param request
   */
  private void handleThumbnailUpdate(PropertyUpdateRequest request) {
    if (request.getThumbnail() == null || request.getThumbnail().isEmpty()) return;
    s3Service.deleteByUrl(request.getThumbnailUrl());

    try {
      byte[] data = imageConversionService.convertToWebP(request.getThumbnail());
      String key = "properties/temp_" + System.currentTimeMillis() + "_thumb.webp";
      String url = s3Service.uploadBytes(key, data, "image/webp");
      request.setThumbnailUrl(url);
    } catch (Exception e) {
      throw new RuntimeException("썸네일 업로드 실패", e);
    }
  }

  /**
   * 매물 및 상세 정보 수정
   * @param request
   */
  private void updatePropertyAndDetails(PropertyUpdateRequest request) {
    if (mapper.updateProperty(request) != 1) {
      throw new RuntimeException("매물 업데이트 실패");
    }
    request.getDetail().setPropertyId(request.getId());
    if (detailMapper.updatePropertyDetail(request.getDetail()) != 1) {
      throw new RuntimeException("상세 정보 업데이트 실패");
    }
  }

  /**
   * 이미지 수정 처리 (삭제 및 추가)
   * @param request
   */
  private void handleImageUpdates(PropertyUpdateRequest request) {
    List<String> dbImages = imagesMapper.selectImageUrlsByPropertyId(request.getId());
    List<String> remaining = request.getImageUrls();
    List<String> toDelete = dbImages.stream().filter(url -> !remaining.contains(url)).toList();

    for (String url : toDelete) {
      s3Service.deleteByUrl(url);
      imagesMapper.softDeleteImageUrl(request.getId(), url);
    }

    if (request.getImages() != null) {
      for (MultipartFile image : request.getImages()) {
        try {
          byte[] data = imageConversionService.convertToWebP(image);
          String key = "properties/" + request.getId() + "_" + System.currentTimeMillis() + ".webp";
          String url = s3Service.uploadBytes(key, data, "image/webp");
          if (imagesMapper.insertPropertyImage(request.getId(), url) != 1) {
            throw new RuntimeException("이미지 저장 실패: " + url);
          }
        } catch (Exception e) {
          throw new RuntimeException("이미지 업로드 실패", e);
        }
      }
    }
  }

  /**
   * 태그 수정 처리 (추가 및 삭제)
   * @param request
   */
  private void handleTagsUpdate(PropertyUpdateRequest request) {
    List<String> names = request.getTags().stream().map(Tag::getName).toList();
    if (names.isEmpty()) return;

    mapper.insertIgnoreDuplicates(names);
    List<Tag> newTags = mapper.findTagsByNames(names);
    Set<Long> newIds = newTags.stream().map(Tag::getId).collect(Collectors.toSet());

    List<Tag> oldTags = mapper.getTagsByPropertyId(request.getId());
    Set<Long> oldIds = oldTags.stream().map(Tag::getId).collect(Collectors.toSet());

    Set<Long> toAdd = new HashSet<>(newIds);
    toAdd.removeAll(oldIds);

    Set<Long> toRemove = new HashSet<>(oldIds);
    toRemove.removeAll(newIds);

    if (!toRemove.isEmpty()) mapper.deletePropertyTags(request.getId(), toRemove);

    if (!toAdd.isEmpty()) {
      List<Map<String, Long>> mappings = toAdd.stream()
              .map(id -> {
                Map<String, Long> map = new HashMap<>();
                map.put("propertyId", request.getId());
                map.put("tagId", id);
                return map;
              })
              .collect(Collectors.toList());

      mapper.bulkInsert(mappings);
    }
  }

  /**
   * 매물 전체 정보 조회 (상세 + 이미지 + 태그 포함)
   * @param id
   * @return
   */
  private PropertyVO getFullProperty(Long id) {
    PropertyVO property = mapper.selectPropertyById(id);
    property.setDetail(detailMapper.selectPropertyDetailByPropertyId(id));
    property.setImageUrls(imagesMapper.selectImageUrlsByPropertyId(id));
    property.setTags(mapper.getTags(property));
    return property;
  }
}