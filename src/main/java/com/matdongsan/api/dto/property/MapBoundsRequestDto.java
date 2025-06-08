package com.matdongsan.api.dto.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapBoundsRequestDto {
  private Double swLat;
  private Double swLng;
  private Double neLat;
  private Double neLng;
}
