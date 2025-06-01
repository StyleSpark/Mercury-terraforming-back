package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import java.time.LocalDateTime;

@Data
@Alias("user")
public class UserVO {
  private Long id;
  private String email;
  private String password;
  private String name;
  private String phone; // 사용자가 직접 입력
  private String role;        // 기본값: "USER"
  private LocalDateTime createdAt;
  private String provider;
  private Integer ticket; // 등록권 개수
  private String profile; // 프로필
  private String nickname;
  private String temperature;
}
