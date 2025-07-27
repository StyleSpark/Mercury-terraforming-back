package com.matdongsan.api.dto.community.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "커뮤니티 게시글 댓글/대댓글 목록 조회 요청 DTO")
public class CommentGetRequest {

  @Schema(description = "댓글/대댓글 ID", example = "1")
  private Long id;

  @Schema(description = "커뮤니티 ID", example = "1")
  private Long communityId;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "부모 댓글 ID", example = "1")
  private Long parentId;

  @Schema(description = "댓글 내용", example = "이러한 상황도 있었어요 ...")
  private String content;

  @Schema(description = "조회할 페이지 번호 (1부터 시작)", example = "1", defaultValue = "1")
  private int page = 1;

  @Schema(description = "페이지당 데이터 수", example = "5", defaultValue = "5")
  private int size = 5;

  public int getOffset() {
    return (page - 1) * size;
  }
}
