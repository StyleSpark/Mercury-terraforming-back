package com.matdongsan.api.dto.user;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserDto {
  private Long id;
  private String email;
  private String nickname;
  private String phone;
  private String profile;
  private String password;

  private MultipartFile image;
}

