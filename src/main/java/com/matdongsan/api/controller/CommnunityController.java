package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.community.*;
import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.CommunityService;
import com.matdongsan.api.vo.CommunityVO;
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

  @Operation(summary = "커뮤니티 게시글 목록 조회", description = "커뮤니티 목록을 페이지네이션 형식으로 조회합니다.")
  @GetMapping
  public ResponseEntity<?> getCommunities(
          @ModelAttribute CommunityGetRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    Long loginUserId = (user != null) ? user.getId() : null;
    Map<String, Object> response = service.getCommunityListWithPagination(request, loginUserId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(summary = "커뮤니티 게시글 상세 조회", description = "커뮤니티 ID로 상세 정보를 조회합니다.")
  @GetMapping("/{communityId}")
  public ResponseEntity<?> getCommunityDetail(
          @Parameter(description = "커뮤니티 ID", example = "1") @PathVariable Long communityId,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    Long loginUserId = (user != null) ? user.getId() : null;
    CommunityVO community = service.getCommunityDetail(communityId, loginUserId);
    return ResponseEntity.ok(ApiResponse.success(community));
  }

  @Operation(summary = "커뮤니티 게시글 수정",
          description = "커뮤니티 게시글을 수정합니다. JWT 인증 필요",
          security = @SecurityRequirement(name = "JWT"))
  @PatchMapping("/{communityId}")
  public ResponseEntity<?> updateCommunity(
          @Parameter(description = "커뮤니티 ID", example = "1") @PathVariable Long communityId,
          @RequestPart("request") CommunityUpdateRequest request,
          @RequestPart(value = "images", required = false) List<MultipartFile> images,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    Long loginUserId = user.getId();
    request.setId(communityId);
    service.updateCommunity(request, images, loginUserId);
    return ResponseEntity.ok(ApiResponse.success("게시글 수정이 성공적으로 완료되었습니다."));
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
