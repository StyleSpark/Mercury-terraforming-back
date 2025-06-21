package com.matdongsan.api.dto.notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCreateRequest {

  @Schema(description = "공지 ID (수정 시 사용)", example = "1001")
  private Long id;

  @Schema(description = "공지 제목", example = "시스템 점검 안내")
  private String title;

  @Schema(description = "공지 내용", example = "2025년 7월 1일 새벽 2시부터 5시까지 시스템 점검이 진행됩니다.")
  private String content;

  @Schema(description = "작성자", example = "관리자")
  private String author;
}
