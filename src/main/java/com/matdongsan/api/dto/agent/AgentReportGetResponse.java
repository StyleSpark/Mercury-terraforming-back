package com.matdongsan.api.dto.agent;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AgentReportGetResponse {
  private String agentName;
  private String targetType;
  private String content;
  private LocalDate createdAt;
}
