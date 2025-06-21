package com.matdongsan.api.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeUpdateRequest {

  @Schema(description = "수정할 공지 ID", example = "1001")
  private Long id;

  @Schema(description = "변경할 제목", example = "점검 시간 변경 안내")
  private String title;

  @Schema(description = "변경할 내용", example = "점검 시간이 1시간 연장되었습니다.")
  private String content;

  @Schema(description = "수정 요청 관리자 ID 또는 이름", example = "admin001")
  private String admin;
}

