package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import java.time.LocalDateTime;

@Data
@Alias("user")
public class UserVO {
  private Long id;  // 추가
  private String name;
  private String email;
  private Integer age;
  private LocalDateTime createdAt;  // 추가
  }