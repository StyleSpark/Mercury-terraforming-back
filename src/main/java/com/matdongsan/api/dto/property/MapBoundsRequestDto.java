package com.matdongsan.api.dto.property;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapBoundsRequestDto {

  @Schema(description = "남서쪽 위도", example = "37.4800")
  private Double swLat;

  @Schema(description = "남서쪽 경도", example = "126.8800")
  private Double swLng;

  @Schema(description = "북동쪽 위도", example = "37.5000")
  private Double neLat;

  @Schema(description = "북동쪽 경도", example = "126.9900")
  private Double neLng;

  @Schema(description = "페이지 번호 (1부터 시작)", example = "1")
  private int page = 1;

  @Schema(description = "한 페이지에 가져올 데이터 개수", example = "10")
  private int size = 10;

  public int getOffset() {
    return (page - 1) * size;
  }
}
