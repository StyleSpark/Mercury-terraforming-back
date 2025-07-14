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
  private String userName;
  private String profile;
  private Long parentId;
  private String content;
  private Long likeCount;
  private Long dislikeCount;
  private Long replyCount;
  private Boolean isMine;
  private String myReaction;
  private LocalDate createdAt;
}
