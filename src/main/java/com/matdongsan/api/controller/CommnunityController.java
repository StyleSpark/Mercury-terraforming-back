package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.community.CommunityCreateRequest;
import com.matdongsan.api.dto.community.CommunityDeleteRequest;
import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.dto.community.CommunityUpdateRequest;
import com.matdongsan.api.service.CommunityService;
import com.matdongsan.api.vo.CommunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommnunityController {

  private final CommunityService service;

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

  /**
   * 커뮤니티 전체 조회
   * @param request 특정 커뮤니티 검색 매개변수
   * @return 페이지와 커뮤니티 리스트
   */
  @GetMapping
  public ResponseEntity<?> getCommunities(CommunityGetRequest request) {
    Map<String, Object> response = service.getCommunityListWithPagination(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 커뮤니티 등록
   * @param request 등록할 커뮤니티 데이터
   * @return 등록 결과
   */
  @PostMapping
  public ResponseEntity<?> createCommunity(@RequestBody CommunityCreateRequest request) {
    // TODO: 사용자 정보 구현 -> 요청 JSON 수정 필요 (임시)
    // TODO: 카테고리 등록 구현 -> '자취 후기', '매물 후기', '꿀팁 공유', '질문 있어요'
    Long id = service.createCommunity(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, id));
  }

  /**
   * 커뮤니티 수정
   * @param id 해당 커뮤니티 id
   * @param request 커뮤니티 데이터
   * @return 수정 결과
   */
  @PatchMapping("/{id}")
  public ResponseEntity<?> updateCommunity(@PathVariable Long id, @RequestBody CommunityUpdateRequest request) {
    service.updateCommunity(id, request);
    return ResponseEntity.ok(ApiResponse.success("커뮤니티 글이 수정되었습니다."));
  }

  /**
   * 커뮤니티 삭제
   * @param id 해당 커뮤니티 id
   * @param request 사용자 정보 데이터
   * @return 삭제 결과
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCommunity(@PathVariable Long id, @RequestBody CommunityDeleteRequest request) {
    service.deleteCommunity(id, request);
    return ResponseEntity.ok(ApiResponse.success("커뮤니티 글이 삭제되었습니다."));
  }
}
