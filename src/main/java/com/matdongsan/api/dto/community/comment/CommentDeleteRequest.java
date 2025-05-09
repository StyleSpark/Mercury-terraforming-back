package com.matdongsan.api.dto.community.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteRequest {
  private Long id;
  private Long userId;
}
