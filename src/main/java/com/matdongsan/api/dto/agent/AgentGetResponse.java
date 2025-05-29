package com.matdongsan.api.dto.agent;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AgentGetResponse {
  private Long agentId;
  private String agentName;
  private String agentProfileUrl;
  private String officeUrl;
  private String brandName;
  private String address;
}
