package com.matdongsan.api.dto.agent;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgentGetResponse {
  private Long id;
  private String name;
  private String profileUrl;
  private String phone;
  private Double temperature;
  private String nickname;
  private String thumbnail;
  private String brand;
  private String address;
  private Double reviewCount;
  private Double reviewAvg;
  private Double latitude;
  private Double longitude;
  private Double distance;
}
