package com.matdongsan.api.dto.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDeleteRequest {
  private Long id;
  private Long userId;
}
