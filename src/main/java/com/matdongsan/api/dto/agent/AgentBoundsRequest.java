package com.matdongsan.api.dto.agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "지도에서 중개인 조회를 위한 영역(Bounding Box) 요청 DTO")
public class AgentBoundsRequest {

  @Schema(description = "남서쪽 위도 (South-West Latitude)", example = "37.560")
  private Double swLat;

  @Schema(description = "남서쪽 경도 (South-West Longitude)", example = "126.970")
  private Double swLng;

  @Schema(description = "북동쪽 위도 (North-East Latitude)", example = "37.570")
  private Double neLat;

  @Schema(description = "북동쪽 경도 (North-East Longitude)", example = "126.980")
  private Double neLng;
}
