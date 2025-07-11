package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.property.*;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.PropertyService;
import com.matdongsan.api.vo.PropertyMarkerVO;
import com.matdongsan.api.vo.PropertyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/properties")
@Tag(name = "매물 API", description = "매물 등록, 조회, 수정, 삭제 API")
public class PropertyController {

  private final PropertyService service;

  @Operation(
          summary = "매물 등록",
          description = "사용자가 새로운 매물을 등록합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping
  public ResponseEntity<?> createProperty(
          @RequestPart("request") PropertyCreateRequest request,
          @RequestPart(value = "images", required = false) List<MultipartFile> images,
          @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setImages(images);
    request.setUserId(user.getId());
    request.setThumbnail(thumbnail);
    request.setStatus("ACTIVE");
    Long id = service.createProperty(request);
    return ResponseEntity.ok(ApiResponse.success(id));
  }

  @Operation(summary = "매물 리스트 조회", description = "조건에 맞는 매물 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<?> getProperties(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @ModelAttribute PropertyGetRequest request) {

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

  @Operation(summary = "매물 상세 조회", description = "매물 ID를 통해 상세 정보를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<?> getPropertyDetail(
          @Parameter(description = "매물 ID", example = "101") @PathVariable Long id,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    PropertyVO property = service.getPropertyDetail(id);
    if (user != null) {
      Boolean isFavorite = service.existsFavorite(user.getId(), id);
      property.setIsFavorite(isFavorite);
    }
    return ResponseEntity.ok(ApiResponse.success(property));
  }

  @Operation(
          summary = "매물 수정",
          description = "등록한 매물 정보를 수정합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PatchMapping("/{id}")
  public ResponseEntity<?> updateProperty(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @RequestPart("request") PropertyUpdateRequest request,
          @RequestPart(value = "images", required = false) List<MultipartFile> images,
          @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {

    request.setUserId(user.getId());
    if (thumbnail != null && !thumbnail.isEmpty()) {
      request.setThumbnail(thumbnail);
    }
    if (images != null && !images.isEmpty()) {
      request.setImages(images);
    }

    PropertyVO property = service.updateProperty(request);
    return ResponseEntity.ok(ApiResponse.success(property));
  }

  @Operation(
          summary = "매물 삭제 (소프트)",
          description = "매물 상태를 비활성화 처리하여 삭제합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProperty(
          @Parameter(description = "삭제할 매물 ID", example = "101") @PathVariable Long id,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    PropertyDeleteRequest request = new PropertyDeleteRequest();
    request.setId(id);
    request.setUserId(user.getId());

    boolean result = service.deleteProperty(request);
    return ResponseEntity.ok(ApiResponse.success(result));
  }

  @Operation(summary = "지도 영역 내 매물 조회", description = "지도의 범위 내에 위치한 매물 목록을 조회합니다.")
  @GetMapping("/withinBounds")
  public ResponseEntity<?> getPropertyWithinMap(@ModelAttribute MapBoundsRequestDto request) {
    Map<String, Object> data = service.getPropertiesWithinBounds(request);
    return ResponseEntity.ok(ApiResponse.success(data));
  }

  @Operation(summary = "지도 영역 내 매물 마커만 조회", description = "지도에 마커로 표시할 최소한의 매물 정보만 조회합니다.")
  @GetMapping("/markers")
  public ResponseEntity<?> getPropertyMarkers(@ModelAttribute MapBoundsRequestDto request) {
    List<PropertyMarkerVO> markers = service.getPropertyMarkersWithinBounds(request);
    return ResponseEntity.ok(ApiResponse.success(markers));
  }
}
