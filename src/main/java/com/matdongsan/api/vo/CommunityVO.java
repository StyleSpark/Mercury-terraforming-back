package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.util.List;

@Data
@Alias("community")
public class CommunityVO {
  private Long id;
  private Long userId;
  private Integer categoryId;
  private String title;
  private String content;
  private List<String> imageUrls;
  private Integer viewCount;
  private LocalDate createdAt;

  private String userName;
  private Long likeCount;
  private Long dislikeCount;
  private Long commentCount;
  private String thumbnailUrl;

  private Boolean isMine;
}
