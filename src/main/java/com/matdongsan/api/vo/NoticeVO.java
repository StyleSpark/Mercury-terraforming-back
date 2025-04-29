package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Data
@Alias("notice")
public class NoticeVO {
  private Long id;
  private String title;
  private String content;
  private LocalDate createdAt;
}
