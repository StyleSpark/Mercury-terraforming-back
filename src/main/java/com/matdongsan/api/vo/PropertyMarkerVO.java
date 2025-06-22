package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyMarkerVO {
  private Long id;
  private Double latitude;
  private Double longitude;
  private Integer price;
  private String thumbnailUrl;
}