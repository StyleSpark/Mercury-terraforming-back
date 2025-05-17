package com.matdongsan.api.controller;

import com.matdongsan.api.dto.user.SocialLoginDto;
import com.matdongsan.api.service.SocialAuthService;
import com.matdongsan.api.util.JwtUtil;
import com.matdongsan.api.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

  private final SocialAuthService service;
  private final JwtUtil jwtUtil;

  //소셜 로그인

  /**
   * 소셜 로그인
   * - 현재는 구글만 구현
   * @param request
   * @return
   */
  @PostMapping("/social-login")
  public ResponseEntity<?> socialLogin(@RequestBody SocialLoginDto request) {
    UserVO user = service.handleSocialLogin(request.getProvider(), request.getToken());
    String jwt = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
    return ResponseEntity.ok(Map.of(
            "accessToken", jwt,
            "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "role", user.getRole()
            )
    ));
  }

  // 회원가입

  // 로그인

  // 회원 프로필 조회

  // 대리인 등록 신청

  // 중개인 조회

  // 중개인 삭제

  // 중개인 댓글 작성

  // 중개인 댓글 조회

  // 중개인 댓글 수정

  // 중개인 댓글 삭제
}
