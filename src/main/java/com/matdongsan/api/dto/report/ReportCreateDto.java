package com.matdongsan.api.dto.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCreateDto {
  private Long id;
  private Long userId;
  private Long targetId;
  private String targetType;
  private String category;
  private String content;
}
