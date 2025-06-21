package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.favorite.PropertyFavoriteCreateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.FavoriteService;
import com.matdongsan.api.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "즐겨찾기 API", description = "매물 및 커뮤니티 즐겨찾기 관리 API")
public class FavoriteController {

  private final FavoriteService service;

  @Operation(
          summary = "매물 찜 추가",
          description = "사용자가 특정 매물을 즐겨찾기(찜) 목록에 추가합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/{propertyId}")
  public ResponseEntity<?> addPropertyFavorite(
          @PathVariable Long propertyId,
          @AuthenticationPrincipal UserRole user) {

    PropertyFavoriteCreateDto request = new PropertyFavoriteCreateDto();
    request.setUserId(user.getId());
    request.setPropertyId(propertyId);

    return ApiResponseUtil.ok(service.addPropertyFavorite(request));
  }

  @Operation(
          summary = "매물 찜 제거",
          description = "사용자가 찜한 매물을 즐겨찾기 목록에서 제거합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @DeleteMapping("/{propertyId}")
  public ResponseEntity<?> removePropertyFavorite(
          @PathVariable Long propertyId,
          @AuthenticationPrincipal UserRole user) {

    PropertyFavoriteCreateDto request = new PropertyFavoriteCreateDto();
    request.setUserId(user.getId());
    request.setPropertyId(propertyId);

    return ApiResponseUtil.ok(service.removePropertyFavorite(request));
  }


  // TODO: 찜 목록 조회, 커뮤니티 즐겨찾기 기능 추가 예정
}
