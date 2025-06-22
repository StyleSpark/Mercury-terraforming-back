package com.matdongsan.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AgentMarkVO {
  @Schema(description = "중개인 고유 ID", example = "1")
  private Long id;

  @Schema(description = "위도", example = "37.5665")
  private Double latitude;

  @Schema(description = "경도", example = "126.9780")
  private Double longitude;
}
