package com.matdongsan.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginDto {

  @Schema(description = "소셜 로그인 제공자 (예: google)", example = "google")
  private String provider;

  @Schema(description = "소셜 로그인 토큰 (id_token)", example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
  private String token;
}
