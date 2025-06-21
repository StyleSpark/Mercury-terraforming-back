package com.matdongsan.api.controller;

import com.matdongsan.api.dto.user.SocialLoginDto;
import com.matdongsan.api.dto.user.UpdateUserDto;
import com.matdongsan.api.dto.user.UserLoginDto;
import com.matdongsan.api.dto.user.UserSignupDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.SocialAuthService;
import com.matdongsan.api.util.ApiResponseUtil;
import com.matdongsan.api.util.JwtUtil;
import com.matdongsan.api.vo.PropertyVO;
import com.matdongsan.api.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "사용자 인증 및 마이페이지 API", description = "로그인, 회원가입, 프로필 관리 등 사용자 기능 제공")
public class UserController {

  private final SocialAuthService service;
  private final JwtUtil jwtUtil;

  @PostMapping("/social-login")
  @Operation(summary = "소셜 로그인(구글)", description = "Google OAuth2 토큰을 기반으로 로그인 처리합니다.")
  public ResponseEntity<?> socialLogin(@RequestBody SocialLoginDto request) {
    UserVO user = service.handleSocialLogin(request.getProvider(), request.getToken());
    String jwt = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
    return ApiResponseUtil.ok(Map.of(
            "accessToken", jwt,
            "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "role", user.getRole(),
                    "provider", user.getProvider()
            )
    ));
  }

  @PostMapping("/signup")
  @Operation(summary = "회원가입", description = "일반 사용자가 회원가입을 진행합니다.")
  public ResponseEntity<?> signup(@RequestBody UserSignupDto request) {
    UserVO createdUser = service.signup(request);
    String jwt = jwtUtil.generateToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRole());
    return ApiResponseUtil.ok(Map.of(
            "accessToken", jwt,
            "user", Map.of(
                    "id", createdUser.getId(),
                    "email", createdUser.getEmail(),
                    "name", createdUser.getName(),
                    "role", createdUser.getRole()
            )
    ));
  }

  @PostMapping("/login")
  @Operation(summary = "이메일 로그인", description = "이메일과 비밀번호로 로그인합니다.")
  public ResponseEntity<?> login(@RequestBody UserLoginDto request) {
    UserVO user = service.login(request.getEmail(), request.getPassword());
    String jwt = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
    return ApiResponseUtil.ok(Map.of(
            "accessToken", jwt,
            "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "role", user.getRole()
            )
    ));
  }

  @PostMapping("/nickname-check")
  @Operation(summary = "닉네임 중복 체크", description = "입력한 닉네임이 중복되는지 여부를 확인합니다.")
  public ResponseEntity<?> checkNickname(@RequestBody Map<String, String> request) {
    boolean exists = service.existsByNickname(request.get("nickname"));
    return ApiResponseUtil.ok(Map.of("available", !exists));
  }

  @GetMapping("/my-page")
  @Operation(summary = "내 정보 조회", description = "JWT 토큰을 기반으로 로그인한 사용자의 프로필 정보를 조회합니다.", security = @SecurityRequirement(name = "JWT"))
  public ResponseEntity<?> getMyprofile(@Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {
    UserVO response = service.getUserDataByUserId(user);
    return ApiResponseUtil.ok(response);
  }

  @PutMapping("/my-page")
  @Operation(summary = "내 정보 수정", description = "사용자가 자신의 프로필을 수정합니다. (닉네임, 프로필 이미지 등). JWT 인증 필요.", security = @SecurityRequirement(name = "JWT"))
  public ResponseEntity<?> updateUserProfile(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @RequestPart("user") UpdateUserDto request,
          @RequestPart(value = "file", required = false) MultipartFile file
  ) {
    request.setId(user.getId());
    if (file != null && !file.isEmpty()) {
      request.setImage(file);
    }
    return ApiResponseUtil.ok(service.updateProfile(request));
  }

  @GetMapping("/properties")
  @Operation(summary = "내가 등록한 매물 조회", description = "사용자가 등록한 매물 목록을 조회합니다. JWT 인증 필요.", security = @SecurityRequirement(name = "JWT"))
  public ResponseEntity<?> getPropertiesByUser(@Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {
    List<PropertyVO> response = service.getPropertiesByUser(user.getId());
    return ApiResponseUtil.ok(response);
  }
}
