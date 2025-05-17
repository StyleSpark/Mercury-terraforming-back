package com.matdongsan.api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SocialUser {
  private String email;
  private String name;
  private String pictureUrl;     // 프로필 사진
  private String provider;       // "google"
  private String externalId;     // Google user ID (sub)

  public SocialUser(String email, String name) {
    this.email = email;
    this.name = name;
  }
}

