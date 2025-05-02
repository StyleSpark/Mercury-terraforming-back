package com.matdongsan.api.dto.notice;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeGetRequest {
  private Long id;
  private String title;
}
