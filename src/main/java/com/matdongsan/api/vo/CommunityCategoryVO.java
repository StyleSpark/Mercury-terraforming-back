package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Data
@Alias("community_category")
public class CommunityCategoryVO {
  private Long id;
  private String name;
  private String description;
  private LocalDate createdAt;
}
