package com.matdongsan.api.dto.agent;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AgentReportGetRequest {
  private Long userId;

  private int page = 1;
  private int size = 10;

  public int getOffset() {
    return (page - 1) * size;
  }
}
