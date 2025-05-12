package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Data
@Alias("reaction")
public class ReactionVO {
  private Long id;
  private Long userId;
  private Long targetId;
  private String targetType;
  private String reactionType;
  private LocalDate createdAt;
}
