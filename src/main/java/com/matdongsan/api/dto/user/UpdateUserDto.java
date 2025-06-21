package com.matdongsan.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserDto {

  @Schema(description = "사용자 ID", example = "42")
  private Long id;

  @Schema(description = "이름", example = "홍길동")
  private String name;

  @Schema(description = "이메일 주소", example = "user@example.com")
  private String email;

  @Schema(description = "닉네임", example = "hong22")
  private String nickname;

  @Schema(description = "전화번호", example = "010-1234-5678")
  private String phone;

  @Schema(description = "한줄 소개", example = "열심히 살아가는 개발자입니다.")
  private String profile;

  @Schema(description = "비밀번호", example = "password1234")
  private String password;

  @Schema(description = "프로필 이미지 파일 (선택)", type = "string", format = "binary")
  private MultipartFile image;
}
