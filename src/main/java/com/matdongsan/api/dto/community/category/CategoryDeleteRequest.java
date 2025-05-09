package com.matdongsan.api.dto.community.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDeleteRequest {
  private Long id;
  private String name;
}
