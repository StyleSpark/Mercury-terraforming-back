package com.matdongsan.api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SocialLoginDto {
  private String provider; //제공자
  private String token; //토큰
}
