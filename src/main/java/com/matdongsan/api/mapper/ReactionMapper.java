package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.vo.ReactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReactionMapper {
  ReactionVO selectReaction(ReactionRequest request);

  void updateReaction(ReactionRequest request);

  Long insertReaction(ReactionRequest request);

  Long selectReactionLikeCount(Long communityId, String targetType);

  Long selectReactionDislikeCount(Long communityId, String targetType);

  List<Map<String, Object>> selectReactionCountGroupByTarget(List<Long> targetIds, String targetType);
}
