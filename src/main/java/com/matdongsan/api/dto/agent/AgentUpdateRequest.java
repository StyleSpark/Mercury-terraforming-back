package com.matdongsan.api.dto.agent;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AgentUpdateRequest {
  private String officeName;
  private String address;
  private String addressDetail;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private String profileUrl;
  private String bio;
}
