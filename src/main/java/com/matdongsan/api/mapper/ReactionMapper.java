package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.vo.ReactionVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReactionMapper {
  ReactionVO selectReaction(ReactionRequest request);

  void updateReaction(ReactionRequest request);

  Long insertReaction(ReactionRequest request);
}
