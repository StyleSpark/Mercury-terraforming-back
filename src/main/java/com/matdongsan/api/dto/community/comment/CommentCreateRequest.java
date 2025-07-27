package com.matdongsan.api.dto.community.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "커뮤니티 게시글 댓글/대댓글 등록 요청 DTO")
public class CommentCreateRequest {

  @Schema(description = "댓글/대댓글 ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(description = "커뮤니티 ID", example = "1")
  private Long communityId;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "부모 댓글 ID", example = "1")
  private Long parentId;

  @Schema(description = "댓글 내용", example = "공감합니다!!")
  private String content;
}
