package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.util.List;

@Data
@Alias("community")
public class CommunityVO {
  private Long id;
  private Integer categoryId;
  private Long userId;
  private String userName;
  private String profile;
  private String title;
  private String content;
  private String thumbnailUrl;
  private List<String> imageUrls;
  private Integer viewCount;
  private Long likeCount;
  private Long dislikeCount;
  private Long commentCount;
  private Boolean isMine;
  private String myReaction;
  private LocalDate createdAt;
}
