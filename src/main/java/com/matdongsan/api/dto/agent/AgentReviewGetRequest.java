package com.matdongsan.api.dto.agent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentReviewGetRequest {
  private Long agentId;

  private int page = 1;
  private int size = 10;

  public int getOffset() {
    return (page - 1) * size;
  }
}
