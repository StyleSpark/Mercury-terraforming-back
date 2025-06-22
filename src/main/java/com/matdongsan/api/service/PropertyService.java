package com.matdongsan.api.service;

import com.matdongsan.api.dto.property.*;
import com.matdongsan.api.mapper.PaymentMapper;
import com.matdongsan.api.mapper.PropertyDetailMapper;
import com.matdongsan.api.mapper.PropertyImagesMapper;
import com.matdongsan.api.mapper.PropertyMapper;
import com.matdongsan.api.vo.PropertyMarkerVO;
import com.matdongsan.api.vo.PropertyVO;
import com.matdongsan.api.vo.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
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
    consumeTicket(request.getUserId());
    handleThumbnailUpload(request);
    insertPropertyAndDetails(request);
    uploadAndSaveImages(request);
    handleTags(request.getTags(), request.getId());
    return request.getId();
  }

  public PropertyVO updateProperty(PropertyUpdateRequest request) {
    validatePropertyOwnership(request);
    handleThumbnailUpdate(request);
    updatePropertyAndDetails(request);
    handleImageUpdates(request);
    handleTagsUpdate(request);
    return getFullProperty(request.getId());
  }

  public boolean deleteProperty(PropertyDeleteRequest request) {
    int deleted = mapper.softDeleteProperty(request);
    int detailDeleted = detailMapper.softDeletePropertyDetail(request);
    int imageDeleted = imagesMapper.softDeletePropertyImages(request);
    return deleted > 0 && detailDeleted >= 0 && imageDeleted >= 0;
  }

  @Transactional(readOnly = true)
  public List<PropertyVO> getProperties(PropertyGetRequest request) {
    List<PropertyVO> properties = mapper.selectProperties(request);
    properties.forEach(p -> p.setTags(mapper.getTags(p)));
    return properties;
  }

  @Transactional(readOnly = true)
  public PropertyVO getPropertyDetail(Long id) {
    return getFullProperty(id);
  }

  public Boolean existsFavorite(Long userId, Long propertyId) {
    return detailMapper.existsFavorite(userId, propertyId);
  }

  public Set<Long> getFavoritePropertyIds(Long userId, List<Long> propertyIds) {
    return propertyIds == null || propertyIds.isEmpty() ? Set.of() :
            new HashSet<>(mapper.getFavoritePropertyIds(userId, propertyIds));
  }

  @Transactional(readOnly = true)
  public Map<String, Object> getPropertiesWithinBounds(MapBoundsRequestDto request) {
    List<PropertyVO> list = mapper.selectPropertiesWithinBounds(request);
    int total = mapper.countPropertiesWithinBounds(request);

    return Map.of(
            "properties", list,
            "total", (int) Math.ceil((double) total / request.getSize()),
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  @Transactional(readOnly = true)
  public List<PropertyMarkerVO> getPropertyMarkersWithinBounds(MapBoundsRequestDto request) {
    return mapper.selectPropertyMarkersWithinBounds(request);
  }

  private void consumeTicket(Long userId) {
    if (paymentMapper.consumeTicket(userId) != 1) {
      throw new IllegalStateException("등록권이 없습니다.");
    }
  }

  private void handleThumbnailUpload(PropertyCreateRequest request) {
    MultipartFile thumbnail = request.getThumbnail();
    if (thumbnail != null && !thumbnail.isEmpty()) {
      try {
        byte[] data = imageConversionService.convertToWebP(thumbnail);
        String key = "properties/temp_" + System.currentTimeMillis() + "_thumb.webp";
        String url = s3Service.uploadBytes(key, data, "image/webp");
        request.setThumbnailUrl(url);
      } catch (Exception e) {
        throw new RuntimeException("썸네일 업로드 실패", e);
      }
    }
  }

  private void insertPropertyAndDetails(PropertyCreateRequest request) {
    mapper.insertProperty(request);
    Optional.ofNullable(request.getId()).orElseThrow(() -> new RuntimeException("매물 저장 실패"));

    request.getDetail().setPropertyId(request.getId());
    if (detailMapper.insertPropertyDetail(request.getDetail()) != 1) {
      throw new RuntimeException("상세 저장 실패");
    }
  }

  private void uploadAndSaveImages(PropertyCreateRequest request) {
    List<MultipartFile> images = request.getImages();
    if (images == null || images.isEmpty()) return;

    for (MultipartFile image : images) {
      try {
        byte[] data = imageConversionService.convertToWebP(image);
        String key = "properties/" + request.getId() + "_" + System.currentTimeMillis() + ".webp";
        String url = s3Service.uploadBytes(key, data, "image/webp");
        if (imagesMapper.insertPropertyImage(request.getId(), url) != 1) {
          throw new RuntimeException("이미지 저장 실패: " + url);
        }
        request.getImageUrls().add(url);
      } catch (Exception e) {
        throw new RuntimeException("이미지 업로드 실패", e);
      }
    }
  }

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

  private void validatePropertyOwnership(PropertyUpdateRequest request) {
    if (!mapper.checkPropertyByUserId(request)) {
      throw new SecurityException("본인의 매물만 수정 가능합니다.");
    }
  }

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

  private void updatePropertyAndDetails(PropertyUpdateRequest request) {
    if (mapper.updateProperty(request) != 1) {
      throw new RuntimeException("매물 업데이트 실패");
    }
    request.getDetail().setPropertyId(request.getId());
    if (detailMapper.updatePropertyDetail(request.getDetail()) != 1) {
      throw new RuntimeException("상세 정보 업데이트 실패");
    }
  }

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

  private PropertyVO getFullProperty(Long id) {
    PropertyVO property = mapper.selectPropertyById(id);
    property.setDetail(detailMapper.selectPropertyDetailByPropertyId(id));
    property.setImageUrls(imagesMapper.selectImageUrlsByPropertyId(id));
    property.setTags(mapper.getTags(property));
    return property;
  }
}