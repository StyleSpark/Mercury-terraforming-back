package com.matdongsan.api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.matdongsan.api.dto.user.SocialUser;
import com.matdongsan.api.dto.user.UpdateUserDto;
import com.matdongsan.api.dto.user.UserSignupDto;
import com.matdongsan.api.mapper.PropertyMapper;
import com.matdongsan.api.mapper.UserMapper;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.util.JwtUtil;
import com.matdongsan.api.vo.PropertyVO;
import com.matdongsan.api.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SocialAuthService {

  private final UserMapper mapper;
  private final PropertyMapper propertyMapper;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;

  private final S3Service s3Service;

  private final ImageConversionService imageConversionService;

  @Value("${security.google.client-id}")
  private String googleClientId;

  public UserVO handleSocialLogin(String provider, String token) {
    if (!"google".equalsIgnoreCase(provider)) {
      throw new IllegalArgumentException("지원하지 않는 provider");
    }

    SocialUser socialUser = verifyGoogleToken(token);

    // 이메일로 회원 조회
    UserVO user = mapper.findByEmail(socialUser.getEmail());
    if (user == null) {
      // 신규 회원 가입
      user = new UserVO();
      user.setEmail(socialUser.getEmail());
      user.setName(socialUser.getName());
      user.setProvider("google");
      user.setRole("user");
      mapper.insertUser(user);
    }

    return user;
  }

  private SocialUser verifyGoogleToken(String idTokenString) {
    try {
      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
              GoogleNetHttpTransport.newTrustedTransport(),
              JacksonFactory.getDefaultInstance()
      ).setAudience(Collections.singletonList(googleClientId))
              .build();

      GoogleIdToken idToken = verifier.verify(idTokenString);
      if (idToken == null) {
        throw new RuntimeException("토큰 검증 실패");
      }

      Payload payload = idToken.getPayload();
      return new SocialUser(payload.getEmail(), (String) payload.get("name"));

    } catch (Exception e) {
      throw new RuntimeException("토큰 검증 오류", e);
    }
  }

  public UserVO signup(UserSignupDto request) {
    // 중복 이메일 확인
    if (mapper.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    // 비밀번호 암호화
    String encryptedPassword = passwordEncoder.encode(request.getPassword());

    // 유저 저장
    UserVO user = new UserVO();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPhone(request.getPhone());
    user.setPassword(encryptedPassword);
    user.setRole("USER"); // 기본 역할

    mapper.insertUserWithPassword(user);

    return user;
  }

  public UserVO login(String email, String password) {
    UserVO user = mapper.findByEmail(email);
    if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
    }
    return user;
  }

  public boolean existsByNickname(String nickname) {
    return mapper.existsByNickname(nickname);
  }

  public UserVO getUserDataByUserId(UserRole user) {
    return mapper.selectUserDataById(user.getId());
  }

  public int updateProfile(UpdateUserDto request) {
    try {
      MultipartFile newImage = request.getImage();

      // ✅ 새 이미지가 있는 경우에만 처리
      if (newImage != null && !newImage.isEmpty()) {
        // 기존 이미지 삭제
        String oldProfile = mapper.selectUserProfile(request.getId());
        if (oldProfile != null && !oldProfile.isBlank()) {
          s3Service.deleteByUrl(oldProfile);
        }

        // 새 이미지 업로드
        byte[] webpProfile = imageConversionService.convertToWebP(newImage);
        String key = "properties/temp_" + System.currentTimeMillis() + "_thumb.webp";
        String newUrl = s3Service.uploadBytes(key, webpProfile, "image/webp");
        request.setProfile(newUrl);
      }

      // ✅ DB 업데이트 실행
      return mapper.updateUserProfile(request);

    } catch (Exception e) {
      throw new RuntimeException("프로필 업데이트 실패", e);
    }
  }


  public List<PropertyVO> getPropertiesByUser(Long id) {
    return propertyMapper.selectUserProperties(id);
  }
}
