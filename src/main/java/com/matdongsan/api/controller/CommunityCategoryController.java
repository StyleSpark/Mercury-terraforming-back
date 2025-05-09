package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.community.category.CategoryCreateRequest;
import com.matdongsan.api.dto.community.category.CategoryDeleteRequest;
import com.matdongsan.api.dto.community.category.CategoryGetRequest;
import com.matdongsan.api.dto.community.category.CategoryUpdateRequest;
import com.matdongsan.api.service.CommunityCategoryService;
import com.matdongsan.api.vo.CommunityCategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/categories")
public class CommunityCategoryController {

  private final CommunityCategoryService service;

  // TODO: 관리자 인증 구현 필요

  /**
   * 사용자 & 관리자 커뮤니티 카테고리 목록 조회
   * @param request 카테고리명, 설명
   * @return 카테고리 id, 이름, 설명, 생성일자
   */
  @GetMapping
  public ResponseEntity<?> getCommunityCategories(CategoryGetRequest request) {
    List<CommunityCategoryVO> categories = service.getCommunityCategories(request);
    return ResponseEntity.ok(ApiResponse.success(categories));
  }

  /**
   * 커뮤니티 카테고리 생성 (관리자)
   * @param request 카테고리명, 설명
   * @return 카테고리 id
   */
  @PostMapping
  public ResponseEntity<?> createCommunityCategory(@RequestBody CategoryCreateRequest request) {
    Long id = service.createCommunityCategory(request);
    return ResponseEntity.ok(ApiResponse.success(201, id));
  }

  /**
   * 커뮤니티 카테고리 수정 (관리자)
   * @param id
   * @param request 카테고리명, 설명
   * @return 수정 결과
   */
  @PatchMapping("/{id}")
  public ResponseEntity<?> updateCommunityCategory(
          @PathVariable Long id,
          @RequestBody CategoryUpdateRequest request) {
    request.setId(id);
    service.updateCommunityCategory(request);
    return ResponseEntity.ok(ApiResponse.success("커뮤니티 카테고리가 수정되었습니다."));
  }

  /**
   * 커뮤니티 카테고리 삭제
   * @param id 카테고리 id
   * @param request 카테고리명
   * @return 삭제 결과
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCommunityCategory(
          @PathVariable Long id,
          @RequestBody CategoryDeleteRequest request) {
    request.setId(id);
    service.deleteCommunityCategory(request);
    return ResponseEntity.ok(ApiResponse.success("커뮤니티 카테고리가 삭제되었습니다."));
  }

}
