package com.matdongsan.api.dto.community.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequest {
  private Long id;
  private String name;
  private String description;
}
