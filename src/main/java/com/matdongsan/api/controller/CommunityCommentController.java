package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.community.comment.CommentCreateRequest;
import com.matdongsan.api.dto.community.comment.CommentDeleteRequest;
import com.matdongsan.api.dto.community.comment.CommentGetRequest;
import com.matdongsan.api.dto.community.comment.CommentUpdateRequest;
import com.matdongsan.api.dto.reaction.ReactionCreateRequest;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.CommunityCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "커뮤니티 댓글 API", description = "커뮤니티 게시글의 댓글 등록/조회/수정/삭제 API")
public class CommunityCommentController {

  private final CommunityCommentService service;

  @Operation(
          summary = "게시글 댓글/대댓글 등록",
          description = "커뮤니티 게시글을 등록합니다. JWT 인증 필요",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/communities/{communityId}/comments")
  public ResponseEntity<?> createComment(
          @Parameter(description = "커뮤니티 ID", example = "1") @PathVariable Long communityId,
          @RequestBody CommentCreateRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setCommunityId(communityId);
    request.setUserId(user.getId());
    Long id = service.createComment(request);
    return ResponseEntity.ok(ApiResponse.success(201, id));
  }

  @Operation(
          summary = "게시글 댓글 목록 조회",
          description = "댓글 목록을 페이지네이션 형식으로 조회합니다."
  )
  @GetMapping("/communities/{communityId}/comments")
  public ResponseEntity<?> getCommentList(
          @Parameter(description = "커뮤니티 ID", example = "1") @PathVariable Long communityId,
          @ModelAttribute CommentGetRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setCommunityId(communityId);
    if (user != null) {
      request.setUserId(user.getId());
    }
    Map<String, Object> response = service.getCommentList(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(
          summary = "게시글 대댓글 목록 조회",
          description = "대댓글 목록을 페이지네이션 형식으로 조회합니다."
  )
  @GetMapping("/comments/{commentId}/replies")
  public ResponseEntity<?> getCommentDetailList(
          @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId,
          @ModelAttribute CommentGetRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setParentId(commentId);
    if (user != null) {
      request.setUserId(user.getId());
    }
    Map<String, Object> response = service.getCommentDetailList(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(
          summary = "게시글 댓글/대댓글 수정",
          description = "커뮤니티 게시글에 대한 댓글/대댓글을 수정합니다. JWT 인증 필요",
          security = @SecurityRequirement(name = "JWT")
  )
  @PatchMapping("/comments/{commentId}")
  public ResponseEntity<?> updateComment(
          @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId,
          @RequestBody CommentUpdateRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setId(commentId);
    request.setUserId(user.getId());
    service.updateComment(request);
    return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다."));
  }

  @Operation(
          summary = "게시글 댓글/대댓글 삭제 (소프트)",
          description = "커뮤니티 게시글에 대한 댓글/대댓글을 비활성화 처리하여 삭제합니다. JWT 인증 필요",
          security = @SecurityRequirement(name = "JWT")
  )
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<?> deleteComment(
          @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    CommentDeleteRequest request = new CommentDeleteRequest();
    request.setId(commentId);
    request.setUserId(user.getId());
    service.deleteComment(request);
    return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다."));
  }

  @Operation(
          summary = "커뮤니티 게시글의 댓글/대댓글에 대한 반응 등록 (좋아요/싫어요)",
          description = "댓글/대댓글에 대한 반응을 등록 합니다. JWT 인증 필요",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/comments/{commentId}/reactions")
  public ResponseEntity<?> createCommentReaction(
          @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId,
          @RequestBody ReactionCreateRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setTargetId(commentId);
    request.setUserId(user.getId());
    Long reactionId = service.createCommentReaction(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, reactionId));
  }

}
