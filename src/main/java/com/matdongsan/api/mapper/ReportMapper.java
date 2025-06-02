package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.agent.AgentReportCreateRequest;
import com.matdongsan.api.dto.agent.AgentReportGetRequest;
import com.matdongsan.api.dto.agent.AgentReportGetResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
  void insertAgentReport(AgentReportCreateRequest request);

  List<AgentReportGetResponse> selectReports(AgentReportGetRequest request);

  Integer countReports(AgentReportGetRequest request);
}
