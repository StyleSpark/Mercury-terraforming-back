package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.vo.ReactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReactionMapper {
  List<Map<String, Object>> selectReactionCountGroupByTarget(List<Long> targetIds, String targetType);

  String isMyReation(Long loginUserId, Long targetId ,String targetType);

  Long selectReactionLikeCount(Long communityId, String targetType);

  Long selectReactionDislikeCount(Long communityId, String targetType);

  ReactionVO selectReaction(ReactionRequest request);

  void updateReaction(ReactionRequest request);

  Long insertReaction(ReactionRequest request);
}
