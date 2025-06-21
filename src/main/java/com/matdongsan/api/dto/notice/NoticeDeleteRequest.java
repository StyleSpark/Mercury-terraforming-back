package com.matdongsan.api.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDeleteRequest {

  @Schema(description = "삭제할 공지 ID", example = "1001")
  private Long id;

  @Schema(description = "삭제 요청 관리자 ID 또는 이름", example = "admin001")
  private String admin;
}
