package com.matdongsan.api.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeGetRequest {

  @Schema(description = "공지 ID (단건 조회 시)", example = "1001")
  private Long id;

  @Schema(description = "검색할 제목 키워드", example = "점검")
  private String title;

  @Schema(description = "조회할 페이지 번호 (1부터 시작)", example = "1", defaultValue = "1")
  private int page = 1;

  @Schema(description = "페이지당 데이터 수", example = "10", defaultValue = "10")
  private int size = 10;

  public int getOffset() {
    return (page - 1) * size;
  }
}
