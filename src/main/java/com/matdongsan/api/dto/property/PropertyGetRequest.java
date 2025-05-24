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
}
