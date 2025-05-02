package com.matdongsan.api.dto.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeUpdateRequest {
  private Long id;
  private String title;
  private String content;
  private String admin;
}
