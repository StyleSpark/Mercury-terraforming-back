package com.matdongsan.api.dto.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "커뮤니티 게시글 삭제 요청 DTO")
public class CommunityDeleteRequest {

  @Schema(description = "커뮤니티 ID", example = "1")
  private Long id;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;
}
