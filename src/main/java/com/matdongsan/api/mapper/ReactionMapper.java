package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.reaction.ReactionCreateRequest;
import com.matdongsan.api.vo.ReactionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReactionMapper {
  List<Map<String, Object>> selectReactionCountGroupByTarget(List<Long> targetIds, String targetType);

  String isMyReation(Long loginUserId, Long targetId ,String targetType);

  Long selectReactionLikeCount(Long communityId, String targetType);

  Long selectReactionDislikeCount(Long communityId, String targetType);

  ReactionVO selectReaction(ReactionCreateRequest request);

  int updateReaction(ReactionCreateRequest request);

  Long insertReaction(ReactionCreateRequest request);
}
