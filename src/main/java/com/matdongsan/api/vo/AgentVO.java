package com.matdongsan.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Alias("agent")
@Schema(description = "중개인 정보 응답 VO")
public class AgentVO {

  @Schema(description = "중개인 고유 ID", example = "1001")
  private Long id;

  @Schema(description = "사용자 고유 ID", example = "2001")
  private Long userId;

  @Schema(description = "중개인 이름", example = "홍길동")
  private String agentName;

  @Schema(description = "사무소 브랜드명", example = "우리공인중개사")
  private String brand;

  @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
  private String address;

  @Schema(description = "상세 주소", example = "3층 301호")
  private String addressDetail;

  @Schema(description = "위도", example = "37.4999")
  private BigDecimal latitude;

  @Schema(description = "경도", example = "127.0352")
  private BigDecimal longitude;

  @Schema(description = "사무소 대표 이미지 URL", example = "https://cdn.site.com/images/agent-profile.jpg")
  private String profileUrl;

  @Schema(description = "중개인 등록일자", example = "2024-06-01")
  private LocalDate createdAt;

  @Schema(description = "중개사 자격증 번호", example = "11-서울-123456")
  private String licenseNumber;

  @Schema(description = "리뷰 평균 평점", example = "4.8")
  private BigDecimal reviewAvg;

  @Schema(description = "리뷰 수", example = "25")
  private Integer reviewCount;
}
