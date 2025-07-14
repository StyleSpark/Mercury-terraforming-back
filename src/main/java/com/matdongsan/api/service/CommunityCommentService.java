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

  private final String targetType = "COMMENT";

  public Long createComment(CommentCreateRequest request) {
    if (request.getUserId() == null) {
      throw new RuntimeException("로그인을 하셔야 댓글을 등록할 수 있습니다.");
    }
    communityCommentMapper.insertCommunityComment(request);
    return request.getId();
  }

  // 댓글 목록 조회
  @Transactional(readOnly = true)
  public Map<String, Object> getCommentList(CommentGetRequest request) {
    List<CommunityCommentVO> comments = communityCommentMapper.selectComments(request);

    if (request.getUserId() != null) {
      for (CommunityCommentVO comment : comments) {
        comment.setIsMine(comment.getUserId().equals(request.getUserId()));
        comment.setMyReaction(reactionMapper.isMyReation(request.getUserId(), comment.getId(), targetType));
      }
    } else {
      for (CommunityCommentVO comment : comments) {
        comment.setIsMine(false);
        comment.setMyReaction("DEFAULT");
      }
    }

    Integer total = communityCommentMapper.countCommunityComments(request);

    return Map.of(
            "comments", comments,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  // 대댓글 목록 조회
  @Transactional(readOnly = true)
  public Map<String, Object> getCommentDetailList(CommentGetRequest request) {
    List<CommunityCommentVO> replies = communityCommentMapper.selectCommentReplies(request);

    if (request.getUserId() != null) {
      for (CommunityCommentVO reply : replies) {
        reply.setIsMine(reply.getUserId().equals(request.getUserId()));
        reply.setMyReaction(reactionMapper.isMyReation(request.getUserId(), reply.getId(), targetType));
      }
    } else {
      for (CommunityCommentVO reply : replies) {
        reply.setIsMine(false);
        reply.setMyReaction("DEFAULT");
      }
    }

    Integer total = communityCommentMapper.countCommentReplies(request);
    return Map.of(
            "replies", replies,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  // 댓글 수정
  public void updateComment(CommentUpdateRequest request) {
    if (request.getUserId() == null) {
      throw new RuntimeException("로그인을 하셔야 댓글을 등록할 수 있습니다.");
    }
    if (communityCommentMapper.updateComment(request) != 1) {
      throw new RuntimeException("작성자만이 해당 내용을 수정할 수 있습니다.");
    }
  }

  // 댓글 삭제
  public void deleteComment(CommentDeleteRequest request) {
    if (request.getUserId() == null) {
      throw new RuntimeException("로그인을 하셔야 댓글을 등록할 수 있습니다.");
    }
    if (communityCommentMapper.softDeleteComment(request) != 1) {
      throw new RuntimeException("작성자만이 해당 내용을 수정할 수 있습니다.");
    }

  }

  // 댓글에 대한 반응 등록
  public Long createCommentReaction(ReactionCreateRequest request) {
    request.setTargetType(targetType);
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
