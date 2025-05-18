package com.matdongsan.api.dto.community.category;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CategoryGetRequest {
  private String name;
  private String description;
}
