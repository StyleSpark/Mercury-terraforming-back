package com.matdongsan.api.dto.property;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyGetRequest {

  @Schema(description = "검색 키워드", example = "풀옵션 원룸")
  private String search;

  @Schema(description = "페이지 크기", example = "10")
  private int size;

  @Schema(description = "현재 페이지", example = "1")
  private int page;

  @Schema(description = "사용자 ID (선택적)", example = "5")
  private Long userId;

  @Schema(description = "현재 위도", example = "37.5665")
  private Double latitude;

  @Schema(description = "현재 경도", example = "126.9780")
  private Double longitude;

  @Schema(description = "조회 반경 (미터)", example = "2000")
  private Double radius;
}
