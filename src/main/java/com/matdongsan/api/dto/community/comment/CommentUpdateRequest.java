package com.matdongsan.api.dto.community.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "커뮤니티 게시글 댓글/대댓글 수정 요청 DTO")
public class CommentUpdateRequest {

  @Schema(description = "댓글/대댓글 ID", example = "1")
  private Long id;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "댓글 내용", example = "수정한 내용입니다.")
  private String content;
}
