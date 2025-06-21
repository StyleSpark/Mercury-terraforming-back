package com.matdongsan.api.dto.agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "중개인 등록 요청 DTO")
public class AgentRegisterRequest {

  @Schema(description = "로그인 사용자 ID (JWT에서 추출됨)", example = "101", hidden = true)
  private Long userId;

  @Schema(description = "중개업자명 (공인중개사 본인 이름)", example = "홍길동", required = true)
  private String agentName;

  @Schema(description = "사무소명", example = "우리공인중개사", required = true)
  private String officeName;

  @Schema(description = "법인등록번호 (13자리 숫자)", example = "1234567890123", required = true)
  private String jurirno;

  @Schema(description = "주소 (도로명 주소 또는 지번 주소)", example = "서울특별시 강남구 테헤란로 123")
  private String address;

  @Schema(description = "상세 주소", example = "3층 301호")
  private String addressDetail;

  @Schema(description = "위도", example = "37.4999")
  private BigDecimal latitude;

  @Schema(description = "경도", example = "127.0352")
  private BigDecimal longitude;

  @Schema(description = "대표 이미지 URL (업로드 완료 후 S3 URL 등으로 대체됨)", example = "https://cdn.site.com/images/office.jpg")
  private String profileUrl;

  @Schema(description = "중개사 자기소개", example = "20년 경력의 지역 전문가입니다.")
  private String bio;

  @Schema(description = "프로필 이미지 (multipart 파일 업로드)", type = "string", format = "binary")
  private MultipartFile profileImage;
}
