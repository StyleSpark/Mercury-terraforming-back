package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.comment.*;
import com.matdongsan.api.dto.reaction.ReactionCreateRequest;
import com.matdongsan.api.mapper.CommunityCommentMapper;
import com.matdongsan.api.mapper.ReactionMapper;
import com.matdongsan.api.vo.CommunityCommentVO;
import com.matdongsan.api.vo.ReactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommunityCommentService {

  private final CommunityCommentMapper communityCommentMapper;
  private final ReactionMapper reactionMapper;

  /**
   * 커뮤니티 댓글/대댓글 등록
   *
   * @param request id, 커뮤니티 id, 유저 id, 부모 댓글 id, 내용
   * @return id
   */
  public Long createComment(CommentCreateRequest request) {
    communityCommentMapper.insertCommunityComment(request);
    return request.getId();
  }

  /**
   * 특정 커뮤니티 댓글 목록 조회
   *
   * @param request 커뮤니티 id, 유저 id, 부모 댓글 id, 내용
   * @return 커뮤니티 id, 유저 id, 부모 댓글 id, 내용, 생성일자, 페이지, 사이즈
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getCommentList(CommentGetRequest request) {
    List<CommunityCommentVO> data = communityCommentMapper.selectComments(request);

    List<Long> targetIds = new ArrayList<>();
    for (CommunityCommentVO comment : data) {
      targetIds.add(comment.getId());
    }

    String targetType = "COMMENT";
    List<Map<String, Object>> counts = reactionMapper.selectReactionCountGroupByTarget(targetIds, targetType);

    Map<Long, Long> likeMap = new HashMap<>();
    Map<Long, Long> dislikeMap = new HashMap<>();

    for (Map<String, Object> row : counts) {
      Long targetId = ((Number) row.get("target_id")).longValue();
      Long likeCount = ((Number) row.get("like_count")).longValue();
      Long dislikeCount = ((Number) row.get("dislike_count")).longValue();
      likeMap.put(targetId, likeCount);
      dislikeMap.put(targetId, dislikeCount);
    }

    List<CommentGetResponse> comments = new ArrayList<>();
    for (CommunityCommentVO vo : data) {
      CommentGetResponse response = CommentGetResponse.builder()
              .id(vo.getId())
              .communityId(vo.getCommunityId())
              .userId(vo.getUserId())
              .parentId(vo.getParentId())
              .content(vo.getContent())
              .createdAt(vo.getCreatedAt())
              .likeCount(likeMap.getOrDefault(vo.getId(), 0L))
              .dislikeCount(dislikeMap.getOrDefault(vo.getId(), 0L))
              .build();

      comments.add(response);
    }

    Integer total = communityCommentMapper.countCommunityComments(request);
    return Map.of(
            "comments", comments,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 특정 커뮤니티 댓글의 댓글 목록 조회
   *
   * @param request 커뮤니티 id, 유저 id, 부모 댓글 id, 내용, 페이지, 사이즈
   * @return 커뮤니티 id, 유저 id, 부모 댓글 id, 내용, 생성일자, 페이지, 사이즈
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getCommentDetailList(CommentGetRequest request) {
    List<CommunityCommentVO> data = communityCommentMapper.selectCommentReplies(request);

    List<Long> targetIds = new ArrayList<>();
    for (CommunityCommentVO comment : data) {
      targetIds.add(comment.getId());
    }

    String targetType = "COMMENT";
    List<Map<String, Object>> counts = reactionMapper.selectReactionCountGroupByTarget(targetIds, targetType);

    Map<Long, Long> likeMap = new HashMap<>();
    Map<Long, Long> dislikeMap = new HashMap<>();

    for (Map<String, Object> row : counts) {
      Long targetId = ((Number) row.get("target_id")).longValue();
      Long likeCount = ((Number) row.get("like_count")).longValue();
      Long dislikeCount = ((Number) row.get("dislike_count")).longValue();
      likeMap.put(targetId, likeCount);
      dislikeMap.put(targetId, dislikeCount);
    }

    List<CommentGetResponse> replies = new ArrayList<>();
    for (CommunityCommentVO vo : data) {
      CommentGetResponse response = CommentGetResponse.builder()
              .id(vo.getId())
              .communityId(vo.getCommunityId())
              .userId(vo.getUserId())
              .parentId(vo.getParentId())
              .content(vo.getContent())
              .createdAt(vo.getCreatedAt())
              .likeCount(likeMap.getOrDefault(vo.getId(), 0L))
              .dislikeCount(dislikeMap.getOrDefault(vo.getId(), 0L))
              .build();

      replies.add(response);
    }

    Integer total = communityCommentMapper.countCommentReplies(request);
    return Map.of(
            "replies", replies,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 커뮤니티 댓글 수정
   *
   * @param request 커뮤니티 댓글 id, 유저 id, 댓글 내용
   */
  public void updateComment(CommentUpdateRequest request) {
    communityCommentMapper.updateComment(request);
  }

  /**
   * 커뮤니티 댓글 삭제
   *
   * @param request 커뮤니티 댓글 id, 유저 id
   */
  public void deleteComment(CommentDeleteRequest request) {
    communityCommentMapper.softDeleteComment(request);
  }

  /**
   * 커뮤니티 댓글 반응(좋아요/싫어요) 등록
   * @param request 커뮤니티 댓글 id, 유저 id, reactionType
   * @return reations id
   */
  public Long createCommentReaction(ReactionCreateRequest request) {
    String reactionType = request.getReactionType().toUpperCase();

    ReactionVO existing = reactionMapper.selectReaction(request);

    if (existing == null) return reactionMapper.insertReaction(request);

    if (reactionType.equals(existing.getReactionType())) {
      request.setReactionType("DEFAULT");
      reactionMapper.updateReaction(request);
      return existing.getId();
    } else {
      reactionMapper.updateReaction(request);
      return existing.getId();
    }
  }
}
