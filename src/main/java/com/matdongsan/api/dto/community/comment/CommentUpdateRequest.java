package com.matdongsan.api.dto.community.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {
  private Long id;
  private Long userId;
  private String content;
}
