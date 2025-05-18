package com.matdongsan.api.dto.community.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
  private Long id;
  private Long communityId;
  private Long userId;
  private Long parentId;
  private String content;
}
