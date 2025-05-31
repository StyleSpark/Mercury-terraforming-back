package com.matdongsan.api.dto.agent;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AgentRegisterRequest {
  // 중개인 확인을 위해 필요한 필드
  private Long userId;            // 로그인 사용자 ID (추후 JWT로 추출해도 됨)
  private String agentName;       // 중개업자명 (brkrNm)
  private String officeName;      // 사무소명 (bsnmCmpnm)
  private String jurirno;         // 법인등록번호 (jurirno)

  // agents 정보를 저장할 필드
  private String address;
  private String addressDetail;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private String profileUrl;       // 사무소 대표 이미지
  private String bio;              // 중개사 소개
}
