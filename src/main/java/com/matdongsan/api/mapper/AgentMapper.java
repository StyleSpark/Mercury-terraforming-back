package com.matdongsan.api.mapper;

import com.matdongsan.api.vo.AgentVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper {
  AgentVO selectAgentDetail(Long agentId);
}
