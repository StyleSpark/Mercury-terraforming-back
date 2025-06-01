package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.agent.*;
import com.matdongsan.api.dto.user.SocialLoginDto;
import com.matdongsan.api.dto.user.UpdateUserDto;
import com.matdongsan.api.dto.user.UserLoginDto;
import com.matdongsan.api.dto.user.UserSignupDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.AgentService;
import com.matdongsan.api.service.SocialAuthService;
import com.matdongsan.api.util.JwtUtil;
import com.matdongsan.api.vo.AgentVO;
import com.matdongsan.api.vo.PropertyVO;
import com.matdongsan.api.vo.UserVO;
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
public class UserController {

  private final SocialAuthService service;
  private final JwtUtil jwtUtil;
  private final AgentService agentService;

  //소셜 로그인

  /**
   * 소셜 로그인
   * - 현재는 구글만 구현
   *
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
                    "role", user.getRole(),
                    "provider", user.getProvider()
            )
    ));
  }

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody UserSignupDto request) {
    UserVO createdUser = service.signup(request);
    String jwt = jwtUtil.generateToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRole());
    return ResponseEntity.ok(Map.of(
            "accessToken", jwt,
            "user", Map.of(
                    "id", createdUser.getId(),
                    "email", createdUser.getEmail(),
                    "name", createdUser.getName(),
                    "role", createdUser.getRole()
            )
    ));
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginDto request) {
    UserVO user = service.login(request.getEmail(), request.getPassword());
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

  /**
   * 닉네임 중복 여부 확인
   *
   * @param request
   * @return
   */
  @PostMapping("/nickname-check")
  public ResponseEntity<?> checkNickname(@RequestBody Map<String, String> request) {
    String nickname = request.get("nickname");
    boolean exists = service.existsByNickname(nickname);
    return ResponseEntity.ok(Map.of("available", !exists));
  }

  /**
   * 내 정보 조회
   * @param user
   * @return
   */
  @GetMapping("/my-page")
  public ResponseEntity<?> getMyprofile(@AuthenticationPrincipal UserRole user) {
    UserVO response = service.getUserDataByUserId(user);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 내 정보 수정
   * @param user
   * @param request
   * @param file
   * @return
   */
  @PutMapping("/my-page")
  public ResponseEntity<?> updateUserProfile(
          @AuthenticationPrincipal UserRole user,
          @RequestPart("user") UpdateUserDto request,
          @RequestPart(value = "file", required = false) MultipartFile file
  ) {
    request.setId(user.getId());
    if (file != null && !file.isEmpty()) {
      request.setImage(file);
    }
    return ResponseEntity.ok(service.updateProfile(request));
  }

  @GetMapping("/properties")
  public ResponseEntity<?> getPropertiesByUser(@AuthenticationPrincipal UserRole user) {
    List<PropertyVO> response = service.getPropertiesByUser(user.getId());
  return ResponseEntity.ok(ApiResponse.success(response));
  }
  // 대리인 등록 신청

  /**
   * 중개인 단일 조회
   * @param agentId 중개인 고유 id
   * @return 중개인 데이터
   */
  @GetMapping("/agents/{agentId}")
  public ResponseEntity<?> getAgentDetail(@PathVariable Long agentId) {
    AgentVO agentDetail = agentService.getAgentDetail(agentId);
    return ResponseEntity.ok(ApiResponse.success(agentDetail));
  }

  /**
   * 중개인 목록 조회
   * @param request 지역:address, 매물명:propertyName, 매물 유형:propertyType, 중개인 이름:agentName, 브랜드명:brandName
   * @return AgentGetResponse, 검색 결과 총 갯수, 페이지, 페이지 사이즈
   */
  @GetMapping("/agents")
  public ResponseEntity<?> getAgents(AgentGetRequest request) {
    Map<String, Object> response = agentService.getAgentListWithPagination(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 중개인 등록 (회원 -> 중개인 전환)
   * @param request AgentRegisterRequest
   * @return 중개인 등록 성공 여부
   */
  @PostMapping("/agents/me")
  public ResponseEntity<?> registerAgent(@RequestBody AgentRegisterRequest request) {
    // TODO: 로그인 사용자 인증 구현 후 수정 필요
    agentService.registerAgent(request);
    return ResponseEntity.ok(ApiResponse.success("중개인 등록 신청이 완료되었습니다."));
  }

  /**
   * 중개인 삭제 (더 이상 '중개인'으로써 활동을 하고 싶지 않을 때라 가정, agents 테이블의 deleted_at 값 변경 / soft-delete)
   * @param request userId TODO: 로그인 사용자 인증 구현 후 수정
   * @return 중개인 삭제 성공 여부
   */
  @DeleteMapping("/agents/me")
  public ResponseEntity<?> deleteAgent(@RequestBody AgentDeleteRequest request) {
    agentService.deleteAgent(request);
    return ResponseEntity.ok(ApiResponse.success("중개인 삭제가 완료되었습니다."));
  }

  /**
   * 중개인 수정 (이직, 사무소 이동, 자기소개 등)
   * @param request AgentUpdateRequest
   * @return 중개인 수정 성공 여부
   */
  @PatchMapping("/agents/me")
  public ResponseEntity<?> updateAgent(@RequestBody AgentUpdateRequest request) {
    Long userId = 12L; // TODO: 로그인 사용자 인증 구현 후 수정
    agentService.updateAgent(request, userId);
    return ResponseEntity.ok(ApiResponse.success("중개인 정보가 수정되었습니다."));
  }

  /**
   * 중개인 리뷰 작성
   * @param agentId 중개인 고유 id
   * @param request AgentReviewCreateRequest (매물 id, 리뷰 내용, 평점)
   * @return 리뷰 작성 성공 여부
   */
  @PostMapping("/agents/{agentId}/reviews")
  public ResponseEntity<?> createReview(@PathVariable Long agentId, @RequestBody AgentReviewCreateRequest request) {
    Long userId = 24L; // TODO: 로그인 사용자 인증 구현 후 수정
    agentService.createReview(agentId, userId, request);
    return ResponseEntity.ok(ApiResponse.success("중개인에 대한 리뷰가 작성되었습니다."));
  }

  // 중개인 리뷰 목록 조회

  /**
   * 특정 중개인의 리뷰 목록 조회
   * @param agentId 중개인 고유 id
   * @param request agentId, 페이지, 사이즈
   * @return @return 사용자 이름, 리뷰 내용, 평점, 생성일자, 리뷰 총 갯수, 페이지, 사이즈
   */
  @GetMapping("/agents/{agentId}/reviews")
  public ResponseEntity<?> getAgentReviews(@PathVariable Long agentId, AgentReviewGetRequest request) {
    request.setAgentId(agentId);
    Map<String, Object> response = agentService.getAgentReviewListWithPagination(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  // 중개인 리뷰 수정

  // 중개인 리뷰 삭제

  // 중개인 신고

  // 중개인 채팅
}
