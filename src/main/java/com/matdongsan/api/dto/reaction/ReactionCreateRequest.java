package com.matdongsan.api.dto.reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "커뮤니티 게시글 반응 등록 요청 DTO")
public class ReactionCreateRequest {

  @Schema(description = "반응 고유 ID", example = "1")
  private Long id;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "적용 대상 ID", example = "1")
  private Long targetId;

  @Schema(description = "적용 타입", example = "'COMMUNITY' or 'COMMENT'")
  private String targetType;

  @Schema(description = "반응 타입", example = "'DEFAULT' or 'LIKE' or 'DISLIKE'")
  private String reactionType;
}
