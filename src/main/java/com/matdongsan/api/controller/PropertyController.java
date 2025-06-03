package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyDeleteRequest;
import com.matdongsan.api.dto.property.PropertyGetRequest;
import com.matdongsan.api.dto.property.PropertyUpdateRequest;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.PropertyService;
import com.matdongsan.api.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/properties")
public class PropertyController {

  final private PropertyService service;

  /**
   * 매물 등록
   * @param request
   * @param images
   * @param thumbnail
   * @param user
   * @return
   */
  @PostMapping
  public ResponseEntity<?> createProperty(
          @RequestPart("request") PropertyCreateRequest request,
          @RequestPart(value = "images", required = false) List<MultipartFile> images,
          @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
          @AuthenticationPrincipal UserRole user) {
    request.setImages(images);
    request.setUserId(user.getId());
    request.setThumbnail(thumbnail);
    request.setStatus("ACTIVE");
    Long id = service.createProperty(request);
    return ResponseEntity.ok(ApiResponse.success(id));
  }
  
  /**
   * 매물 리스트 조회
   * @param request
   * @return
   */
  @GetMapping
  public ResponseEntity<?> getProperties(PropertyGetRequest request, @AuthenticationPrincipal UserRole user) {
    List<PropertyVO> properties = service.getProperties(request);

    if (user != null && !properties.isEmpty()) {
      List<Long> propertyIds = properties.stream()
              .map(PropertyVO::getId)
              .toList();

      Set<Long> favoriteIds = service.getFavoritePropertyIds(user.getId(), propertyIds);

      for (PropertyVO property : properties) {
        property.setIsFavorite(favoriteIds.contains(property.getId()));
      }
    }

    return ResponseEntity.ok(ApiResponse.success(properties));
  }


  /**
   * 매물 상세 조회
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getPropertyDetail(@PathVariable Long id,@AuthenticationPrincipal UserRole user) {
    PropertyVO property = service.getPropertyDetail(id);
    if(user != null){
    Boolean isFavorite = service.existsFavorite(user.getId(),id);
    property.setIsFavorite(isFavorite);
    }
    return ResponseEntity.ok(ApiResponse.success(property));
  }

  /**
   *  매물 수정
   * @param request
   * @param images
   * @param thumbnail
   * @return
   */
  @PatchMapping("/{id}")
  public ResponseEntity<?> updateProperty(@RequestPart("request") PropertyUpdateRequest request,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                          @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {
    PropertyVO property = service.updateProperty(request);
    return ResponseEntity.ok(ApiResponse.success(property));
  }

  /**
   * 매물 삭제 (소프트)
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProperty(@PathVariable Long id,@AuthenticationPrincipal UserRole user) {
    PropertyDeleteRequest request = new PropertyDeleteRequest();
    request.setId(id);

    // 사용자 id는 security 추가후 적용해야함
    request.setUserId(user.getId());
    boolean result = service.deleteProperty(request);
    return ResponseEntity.ok(ApiResponse.success(result));
  }
}
