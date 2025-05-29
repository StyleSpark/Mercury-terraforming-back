package com.matdongsan.api.dto.agent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentGetRequest {

  private String address;         // 지역
  private String propertyName;   // 매물 이름
  private String propertyType;   // 매물 유형
  private String agentName;      // 사용자 이름
  private String brandName;      // 사무소 이름

  // '검색 순서 정렬 필터'를 추가로 고려해야 할지? (리뷰 평점순, 중개 경력순, 거리순)

  private int page = 1;
  private int size = 10;

  public int getOffset() {
    return (page - 1) * size;
  }
}
