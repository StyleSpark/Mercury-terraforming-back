package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.community.*;
import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.service.CommunityService;
import com.matdongsan.api.vo.CommunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommnunityController {

  private final CommunityService service;

  /**
   * 커뮤니티 단일 조회
   * @param communityId 커뮤니티 id
   * @return 커뮤니티 상세 정보
   */
  @GetMapping("/{communityId}")
  public ResponseEntity<?> getCommunityDetail(@PathVariable Long communityId) {
    CommunityGetResponse community = service.getCommunityDetail(communityId);
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
    Long communityId = service.createCommunity(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, communityId));
  }

  /**
   * 커뮤니티 수정
   * @param communityId 커뮤니티 id
   * @param request 커뮤니티 데이터
   * @return 수정 결과
   */
  @PatchMapping("/{communityId}")
  public ResponseEntity<?> updateCommunity(
          @PathVariable Long communityId,
          @RequestBody CommunityUpdateRequest request) {
    request.setId(communityId);
    service.updateCommunity(request);
    return ResponseEntity.ok(ApiResponse.success("커뮤니티 글이 수정되었습니다."));
  }

  /**
   * 커뮤니티 삭제
   * @param communityId 커뮤니티 id
   * @param request 사용자 정보 데이터
   * @return 삭제 결과
   */
  @DeleteMapping("/{communityId}")
  public ResponseEntity<?> deleteCommunity(
          @PathVariable Long communityId,
          @RequestBody CommunityDeleteRequest request) {
    request.setId(communityId);
    service.deleteCommunity(request);
    return ResponseEntity.ok(ApiResponse.success("커뮤니티 글이 삭제되었습니다."));
  }

  /**
   * 커뮤니티 게시글 반응(좋아요/싫어요) 등록
   * @param communityId 커뮤니티 id
   * @param request 유저 id, reactionType
   * @return reations id
   */
  @PostMapping("/{communityId}/reactions")
  public ResponseEntity<?> createCommunityReaction(
          @PathVariable Long communityId,
          @RequestBody ReactionRequest request) {
    request.setTargetId(communityId);
    request.setTargetType("COMMUNITY");
    Long reactionId = service.createCommunityReaction(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, reactionId));
  }

}
