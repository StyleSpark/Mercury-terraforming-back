package com.matdongsan.api.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CommunityGetResponse {
  private Long communityId;
  private Long userId;
  private String userName;
  private Long categoryId;
  private String title;
  private String content;
  private List<String> imageUrls;
  private Integer viewCount;
  private LocalDate createdAt;
  private Long likeCount;
  private Long dislikeCount;
  private Boolean isMine;
}