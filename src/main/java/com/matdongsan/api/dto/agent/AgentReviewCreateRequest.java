package com.matdongsan.api.dto.agent;

import lombok.Getter;

@Getter
public class AgentReviewCreateRequest {
  private Long propertyId;
  private String content;
  private Integer rate;
}
