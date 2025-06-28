package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.community.*;
import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
@Tag(name = "커뮤니티 API", description = "커뮤니티 게시글 등록/조회/수정/삭제 API")
public class CommnunityController {

  private final CommunityService service;

  @Operation(summary = "커뮤니티 게시글 등록",
          description = "커뮤니티 게시글을 등록합니다. JWT 인증 필요",
          security = @SecurityRequirement(name = "JWT"))
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createCommunity(
          @RequestPart("request") CommunityCreateRequest request,
          @RequestPart(value = "images", required = false) List<MultipartFile> images,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    Long loginUserId = user.getId();
    Long id = service.createCommunity(request, images, loginUserId);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, id));
  }

  /**
   * 커뮤니티 단일 조회
   * @param communityId 커뮤니티 id
   * @return 커뮤니티 상세 정보
   */
  @GetMapping("/{communityId}")
  public ResponseEntity<?> getCommunityDetail(
          @AuthenticationPrincipal UserRole user,
          @PathVariable Long communityId) {
    Long loginUserId = user != null ? user.getId() : null;
    CommunityGetResponse community = service.getCommunityDetail(communityId, loginUserId);
    return ResponseEntity.ok(ApiResponse.success(community));
  }

  /**
   * 커뮤니티 전체 조회
   * @param request 특정 커뮤니티 검색 매개변수
   * @return 페이지와 커뮤니티 리스트
   */
  @GetMapping
  public ResponseEntity<?> getCommunities(
          @AuthenticationPrincipal UserRole user,
          CommunityGetRequest request) {
    Long loginUserId = user != null ? user.getId() : null;
    Map<String, Object> response = service.getCommunityListWithPagination(request, loginUserId);
    return ResponseEntity.ok(ApiResponse.success(response));
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
