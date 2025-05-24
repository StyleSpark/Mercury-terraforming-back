package com.matdongsan.api.dto.favorite;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyFavoriteCreateDto {
  private Long id;
  private Long userId;
  private Long propertyId;
}
