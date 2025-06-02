package com.matdongsan.api.dto.agent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentReviewUpdateRequest {
  private Long reviewId;
  private Long userId;
  private String content;
  private Integer rate;
}
