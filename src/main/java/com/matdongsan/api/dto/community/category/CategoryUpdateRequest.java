package com.matdongsan.api.dto.community.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {
  private Long id;
  private String name;
  private String description;
}
