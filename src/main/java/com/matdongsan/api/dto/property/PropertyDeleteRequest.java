package com.matdongsan.api.dto.property;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDeleteRequest {

  @Schema(description = "삭제할 매물 ID", example = "123")
  private Long id;

  @Schema(description = "요청한 사용자 ID", example = "5")
  private Long userId;
}
