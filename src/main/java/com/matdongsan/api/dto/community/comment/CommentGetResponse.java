package com.matdongsan.api.dto.community.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CommentGetResponse {
  private Long id;
  private Long communityId;
  private Long userId;
  private Long parentId;
  private String content;
  private LocalDate createdAt;
  private Long likeCount;
  private Long dislikeCount;
}
