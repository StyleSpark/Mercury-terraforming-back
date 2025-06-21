package com.matdongsan.api.dto.agent;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AgentReviewGetResponse {
  private Long id;
  private Long reviewId;
  private String profile;
  private String name;
  private String content;
  private Double rate;
  private LocalDate createdAt;
}
