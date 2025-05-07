package com.matdongsan.api.dto.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDeleteRequest {
  private Long id;
  private String admin;
}
