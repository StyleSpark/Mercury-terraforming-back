package com.matdongsan.api.dto.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Schema(description = "커뮤니티 게시글 등록 요청 DTO")
public class CommunityCreateRequest {

  @Schema(description = "커뮤니티 ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "카테고리 ID", example = "1")
  private Integer categoryId;

  @Schema(description = "게시글 제목", example = "제목입니다.")
  private String title;

  @Schema(description = "게시글 내용", example = "<p>내용</p><img src='blob:...'>")
  private String content;

  @Schema(description = "blob URL → 파일 index 매핑 정보")
  private Map<String, Integer> blobUrlMap;
}
