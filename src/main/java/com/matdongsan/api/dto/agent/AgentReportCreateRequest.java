package com.matdongsan.api.dto.agent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentReportCreateRequest {
  private Long userId;
  private String targetType;
  private Long targetId;
  private String content;
}
