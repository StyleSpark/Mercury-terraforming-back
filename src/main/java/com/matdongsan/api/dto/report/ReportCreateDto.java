package com.matdongsan.api.dto.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCreateDto {

  @Schema(description = "신고 ID (자동 생성)", example = "null")
  private Long id;

  @Schema(description = "신고자 사용자 ID", example = "42")
  private Long userId;

  @Schema(description = "신고 대상 ID (예: 매물 ID 또는 중개인 ID)", example = "101")
  private Long targetId;

  @Schema(
          description = "신고 대상 종류",
          example = "PROPERTY", // 또는 AGENT
          allowableValues = {"PROPERTY", "AGENT"}
  )
  private String targetType;

  @Schema(
          description = "신고 카테고리",
          example = "허위매물",
          allowableValues = {
                  "허위매물", "욕설/비방", "사기", "불쾌한 언행", "기타"
          }
  )
  private String category;

  @Schema(description = "신고 내용 상세 설명", example = "허위로 가격을 낮게 기재하고 실제 방문 시 다른 가격을 제시함.")
  private String content;
}
