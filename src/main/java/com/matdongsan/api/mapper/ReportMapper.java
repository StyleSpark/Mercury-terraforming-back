package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.agent.AgentReportCreateRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {
  void insertAgentReport(AgentReportCreateRequest request);
}
