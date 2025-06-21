package com.matdongsan.api.dto.agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "중개인 검색 조건 요청 DTO")
public class AgentGetRequest {

  @Schema(description = "검색 지역 (예: 서울특별시 강남구)", example = "서울특별시 강남구")
  private String address;

  @Schema(description = "매물명 (예: 삼성레미안)", example = "삼성레미안")
  private String propertyName;

  @Schema(description = "매물 유형 (예: 오피스텔, 원룸)", example = "오피스텔")
  private String propertyType;

  @Schema(description = "중개인 이름", example = "홍길동")
  private String agentName;

  @Schema(description = "사무소 이름 또는 브랜드명", example = "우리공인중개사")
  private String brandName;

  @Schema(description = "사용자 현재 위치 위도", example = "37.5665")
  private Double latitude;

  @Schema(description = "사용자 현재 위치 경도", example = "126.9780")
  private Double longitude;

  @Schema(description = "검색 반경 (단위: km)", example = "2.0")
  private Double radius;

  @Schema(description = "페이지 번호 (기본값: 1)", example = "1")
  private int page = 1;

  @Schema(description = "페이지당 항목 수 (기본값: 10)", example = "10")
  private int size = 10;

  public int getOffset() {
    return (page - 1) * size;
  }
}
