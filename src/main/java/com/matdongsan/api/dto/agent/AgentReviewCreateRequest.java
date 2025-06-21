package com.matdongsan.api.dto.agent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "중개인 리뷰 작성 요청 DTO")
public class AgentReviewCreateRequest {

  @Schema(description = "작성자 사용자 ID", example = "123", hidden = true)
  private Long userId;

  @Schema(description = "대상 중개인 ID", example = "456", hidden = true)
  private Long agentId;

  @Schema(description = "관련 매물 ID", example = "789", required = true)
  private Long propertyId;

  @Schema(description = "리뷰 내용", example = "매우 친절하게 설명해 주셨어요.", required = true)
  private String content;

  @Schema(description = "평점 (0.0 ~ 5.0)", example = "4.5", required = true)
  private Double rate;
}
