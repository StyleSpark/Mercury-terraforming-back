package com.matdongsan.api.dto.agent;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AgentReviewGetResponse {
  private String userName;
  private String content;
  private Integer rate;
  private LocalDate created_at;
}
