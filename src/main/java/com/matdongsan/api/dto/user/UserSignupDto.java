package com.matdongsan.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDto {

  @Schema(description = "이름", example = "홍길동")
  private String name;

  @Schema(description = "이메일 주소", example = "user@example.com")
  private String email;

  @Schema(description = "전화번호", example = "010-1234-5678")
  private String phone;

  @Schema(description = "비밀번호", example = "password1234")
  private String password;
}
