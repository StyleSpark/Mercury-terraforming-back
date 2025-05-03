package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.service.CommunityService;
import com.matdongsan.api.vo.CommunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommnunityController {

  private final CommunityService service;

  // 커뮤니티 글 단일 조회

  // 커뮤니티 글 전체 조회

  // 커뮤니티 글 등록

  // 커뮤니티 글 수정

  /**
   * 커뮤니티 단일 조회
   * @param id 커뮤니티 id
   * @return 커뮤니티 상세 정보
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getCommunityDetail(@PathVariable Long id) {
    CommunityVO community = service.getCommunityDetail(id);
    return ResponseEntity.ok(ApiResponse.success(community));
  }

}
