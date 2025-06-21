package com.matdongsan.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialUser {

  @Schema(description = "이메일 주소", example = "user@example.com")
  private String email;

  @Schema(description = "이름", example = "홍길동")
  private String name;

  @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
  private String pictureUrl;

  @Schema(description = "소셜 제공자 (예: google)", example = "google")
  private String provider;

  @Schema(description = "소셜 사용자 고유 ID (ex: Google sub)", example = "123456789012345678901")
  private String externalId;

  public SocialUser(String email, String name) {
    this.email = email;
    this.name = name;
  }
}
