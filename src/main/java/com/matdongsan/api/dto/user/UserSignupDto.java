package com.matdongsan.api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDto {
  private String name;
  private String email;
  private String phone;
  private String password;
}
