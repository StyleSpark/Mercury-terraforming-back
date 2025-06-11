package com.matdongsan.api.dto.community;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityGetRequest {
  private Long id;
  private String userName;
  private String title;
  private String content;
  private String titleOrContent;

  private int page = 1; // 기본 1페이지
  private int size = 10; // 기본 페이지 크기

  public int getOffset() {
    return (page - 1) * size;
  }
}
