package com.matdongsan.api.dto.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCreateRequest {
  private Long id;
  private String title;
  private String content;
  private String author;
}
