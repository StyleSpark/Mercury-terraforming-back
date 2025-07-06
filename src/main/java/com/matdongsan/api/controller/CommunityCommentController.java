package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.community.comment.CommentCreateRequest;
import com.matdongsan.api.dto.community.comment.CommentDeleteRequest;
import com.matdongsan.api.dto.community.comment.CommentGetRequest;
import com.matdongsan.api.dto.community.comment.CommentUpdateRequest;
import com.matdongsan.api.dto.reaction.ReactionCreateRequest;
import com.matdongsan.api.service.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommunityCommentController {

  private final CommunityCommentService service;

  // TODO: 로그인 사용자 인증 구현 필요 (임시로 요청 DTO의 userId를 사용)

  /**
   * 커뮤니티 댓글/대댓글 등록
   * @param communityId 커뮤니티 id
   * @param request 사용자 id(임시), 댓글 부모 id, 댓글 내용
   * @return 댓글 고유 id
   */
  @PostMapping("/communities/{communityId}/comments")
  public ResponseEntity<?> createComment(
          @PathVariable Long communityId,
          @RequestBody CommentCreateRequest request) {
    request.setCommunityId(communityId);
    Long id = service.createComment(request);
    return ResponseEntity.ok(ApiResponse.success(201, id));
  }

  /**
   * 특정 커뮤니티 댓글 목록 조회 (오프셋 방식으로 진행)
   * @param communityId 커뮤니티 id
   * @param request 커뮤니티 id, 유저 id, 부모 댓글 id, 내용, 페이지, 사이즈
   * @return 커뮤니티 id, 유저 id, 부모 댓글 id, 내용, 생성일자, 페이지, 사이즈
   */
  @GetMapping("/communities/{communityId}/comments")
  public ResponseEntity<?> getCommentList(
          @PathVariable Long communityId,
          CommentGetRequest request) {
    request.setCommunityId(communityId);
    Map<String, Object> response = service.getCommentList(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 특정 커뮤니티 댓글에 대한 댓글 목록 조회 // TODO: 페이지네이션 커서 방식 기본 5개씩 답글 보여주는 형식으로 생각 중
   * @param commentId 커뮤니티 id
   * @param request 커뮤니티 id, 유저 id, 부모 댓글 id, 내용, 페이지, 사이즈
   * @return 커뮤니티 id, 유저 id, 부모 댓글 id, 내용, 생성일자, 페이지, 사이즈
   */
  @GetMapping("/comments/{commentId}/replies")
  public ResponseEntity<?> getCommentDetailList(
          @PathVariable Long commentId,
          CommentGetRequest request) {
    request.setParentId(commentId);
    Map<String, Object> response = service.getCommentDetailList(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 커뮤니티의 댓글/대댓글 수정
   * @param commentId 댓글 id
   * @param request 댓글 id, 유저 id, 댓글 내용
   * @return 수정 결과
   */
  @PatchMapping("/comments/{commentId}")
  public ResponseEntity<?> updateComment(
          @PathVariable Long commentId,
          @RequestBody CommentUpdateRequest request) {
    request.setId(commentId);
    service.updateComment(request);
    return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다."));
  }

  /**
   * 커뮤니티의 댓글/대댓글 삭제
   * @param commentId 댓글 id
   * @param request 댓글 id, 유저 id, 댓글 내용
   * @return 삭제 결과
   */
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<?> deleteComment(
          @PathVariable Long commentId,
          @RequestBody CommentDeleteRequest request) {
    request.setId(commentId);
    service.deleteComment(request);
    return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다."));
  }

  /**
   * 커뮤니티 댓글 반응(좋아요/싫어요) 등록
   * @param commentId 커뮤니티 댓글 id
   * @param request 유저 id, reactionType
   * @return reations id
   */
  @PostMapping("/comments/{commentId}/reactions")
  public ResponseEntity<?> createCommentReaction(
          @PathVariable Long commentId,
          @RequestBody ReactionCreateRequest request) {
    request.setTargetId(commentId);
    request.setTargetType("COMMENT");
    Long reactionId = service.createCommentReaction(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, reactionId));
  }

}
