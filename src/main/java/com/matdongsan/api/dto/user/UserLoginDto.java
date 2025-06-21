package com.matdongsan.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {

  @Schema(description = "로그인 이메일 주소", example = "user@example.com")
  private String email;

  @Schema(description = "비밀번호", example = "password1234")
  private String password;
}
