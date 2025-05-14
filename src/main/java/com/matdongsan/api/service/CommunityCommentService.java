package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.comment.CommentCreateRequest;
import com.matdongsan.api.dto.community.comment.CommentDeleteRequest;
import com.matdongsan.api.dto.community.comment.CommentGetRequest;
import com.matdongsan.api.dto.community.comment.CommentUpdateRequest;
import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.mapper.CommunityCommentMapper;
import com.matdongsan.api.mapper.ReactionMapper;
import com.matdongsan.api.vo.CommunityCommentVO;
import com.matdongsan.api.vo.ReactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    List<CommunityCommentVO> comments = communityCommentMapper.selectComments(request);
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
    List<CommunityCommentVO> replies = communityCommentMapper.selectCommentReplies(request);
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
  public Long createCommentReaction(ReactionRequest request) {
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
