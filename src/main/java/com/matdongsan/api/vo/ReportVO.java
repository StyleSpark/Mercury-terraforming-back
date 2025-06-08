package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportVO {
  private Long id;
  private String targetType;
  private Long targetId;
  private String content;
  private String category;
  private LocalDateTime createdAt;
}
