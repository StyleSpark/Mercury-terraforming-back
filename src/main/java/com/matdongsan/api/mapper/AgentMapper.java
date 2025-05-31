package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.agent.AgentGetRequest;
import com.matdongsan.api.dto.agent.AgentGetResponse;
import com.matdongsan.api.dto.agent.AgentRegisterRequest;
import com.matdongsan.api.dto.agent.AgentUpdateRequest;
import com.matdongsan.api.vo.AgentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgentMapper {
  AgentVO selectAgentDetail(Long agentId);

  List<AgentGetResponse> selectAgents(AgentGetRequest request);

  Integer countAgents(AgentGetRequest request);

  void insertAgent(AgentRegisterRequest request);

  void softDeleteAgentByUserId(@Param("userId") Long userId);

  void updateAgent(@Param("request") AgentUpdateRequest request, @Param("userId") Long userId);
}
