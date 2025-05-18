package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.community.comment.CommentCreateRequest;
import com.matdongsan.api.dto.community.comment.CommentDeleteRequest;
import com.matdongsan.api.dto.community.comment.CommentGetRequest;
import com.matdongsan.api.dto.community.comment.CommentUpdateRequest;
import com.matdongsan.api.vo.CommunityCommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityCommentMapper {
  Long insertCommunityComment(CommentCreateRequest request);

  List<CommunityCommentVO> selectComments(CommentGetRequest request);

  Integer countCommunityComments(CommentGetRequest request);

  List<CommunityCommentVO> selectCommentReplies(CommentGetRequest request);

  Integer countCommentReplies(CommentGetRequest request);

  void updateComment(CommentUpdateRequest request);

  void softDeleteComment(CommentDeleteRequest request);
}
