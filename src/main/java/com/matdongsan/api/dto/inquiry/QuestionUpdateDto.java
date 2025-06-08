package com.matdongsan.api.dto.inquiry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionUpdateDto {
  private Long id;
  private Long userId;
  private String title;
  private String content;
  private String status;
  private String category;
}
