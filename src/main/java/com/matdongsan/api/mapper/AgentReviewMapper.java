package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.agent.AgentReviewCreateRequest;
import com.matdongsan.api.dto.agent.AgentReviewGetRequest;
import com.matdongsan.api.dto.agent.AgentReviewGetResponse;
import com.matdongsan.api.dto.agent.AgentReviewUpdateRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentReviewMapper {
  boolean existsByUserByAgent(Long userId, Long agentId);

  void insertAgentReview(AgentReviewCreateRequest request);

  List<AgentReviewGetResponse> selectAgentReviews(AgentReviewGetRequest request);

  Integer countAgentReviews(AgentReviewGetRequest request);

  boolean existsByReviewByUser(AgentReviewUpdateRequest request);

  void updateReview(AgentReviewUpdateRequest request);

  void softDeleteAgentReview(Long reviewId, Long userId);
}
