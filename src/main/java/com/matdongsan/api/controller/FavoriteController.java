package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.favorite.PropertyFavoriteCreateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.FavoriteService;
import com.matdongsan.api.vo.TicketVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

  final private FavoriteService service;

  // 매물 찜 등록 & 해제

  /**
   * 매물 즐겨찾기 추가
   * Todo: 유저 비로그인시 예외발생
   * @param request
   * @param user
   * @return
   */
  @PostMapping
  public ResponseEntity<?> addPropertyFavorite(@RequestBody PropertyFavoriteCreateDto request, @AuthenticationPrincipal UserRole user) {
    request.setUserId(user.getId());
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, service.addPropertyFavorite(request)));
  }

  @DeleteMapping
  public ResponseEntity<?> removePropertyFavorite(@RequestBody PropertyFavoriteCreateDto request, @AuthenticationPrincipal UserRole user) {
    request.setUserId(user.getId());
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, service.removePropertyFavorite(request)));
  }
  // 찜 목록 조회

  // 커뮤니티 즐겨찾기 등록 & 해제

  // 커뮤니티 즐겨찾기 조회

}
