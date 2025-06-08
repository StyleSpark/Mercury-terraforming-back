package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class QuestionVO {
  private Long id;
  private Long userId;
  private String title;
  private String status;
  private String content;
  private String category;
  private String answer;
  private LocalDate createdAt;
}
