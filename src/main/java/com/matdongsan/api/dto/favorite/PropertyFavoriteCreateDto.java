package com.matdongsan.api.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyFavoriteCreateDto {

  @Schema(description = "즐겨찾기 ID (DB 저장 후 자동 생성)", example = "101")
  private Long id;

  @Schema(description = "사용자 ID", example = "1001")
  private Long userId;

  @Schema(description = "즐겨찾기할 매물 ID", example = "2001")
  private Long propertyId;
}
