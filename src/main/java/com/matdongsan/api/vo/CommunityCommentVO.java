package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Data
@Alias("community_comments")
public class CommunityCommentVO {
  private Long id;
  private Long communityId;
  private Long userId;
  private Long parentId;
  private String content;
  private LocalDate createdAt;
}
