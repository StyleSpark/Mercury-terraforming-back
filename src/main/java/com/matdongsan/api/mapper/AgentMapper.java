package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.agent.*;
import com.matdongsan.api.vo.AgentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgentMapper {
  AgentVO selectAgentDetail(Long agentId);

  List<AgentGetResponse> selectAgents(AgentGetRequest request);

  Integer countAgents(AgentGetRequest request);

  int insertAgent(AgentRegisterRequest request);

  int softDeleteAgentByUserId(@Param("userId") Long userId);

  int updateAgent(@Param("request") AgentUpdateRequest request, @Param("userId") Long userId);

  List<AgentGetResponse> selectPropertiesWithinBounds(AgentBoundsRequest request);

  Long selectUserIdByAgentId(Long agentId);

  int countPropertiesByAgent(Long userId);

  boolean existAgent(AgentRegisterRequest request);
}
