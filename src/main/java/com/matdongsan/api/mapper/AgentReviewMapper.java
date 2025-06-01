package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.agent.AgentReviewCreateRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentReviewMapper {
  boolean existsByUserByAgent(Long userId, Long agentId);

  void insertAgentReview(Long userId, Long agentId, AgentReviewCreateRequest request);
}
