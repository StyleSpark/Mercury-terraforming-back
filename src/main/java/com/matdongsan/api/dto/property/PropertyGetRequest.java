package com.matdongsan.api.dto.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyGetRequest {
  private String search;
  private int size;
  private int page;
  private Long userId;

  private Double latitude;  // 위도
  private Double longitude; // 경도
  private Double radius;
}
