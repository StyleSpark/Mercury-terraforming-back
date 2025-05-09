package com.matdongsan.api.dto.community.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentGetRequest {
  private Long id;
  private Long communityId;
  private Long userId;
  private Long parentId;
  private String content;

  private int page = 1; // 기본 1페이지
  private int size = 5; // 기본 페이지 크기

  public int getOffset() {
    return (page - 1) * size;
  }
}
