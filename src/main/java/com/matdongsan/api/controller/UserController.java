package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.agent.AgentGetRequest;
import com.matdongsan.api.dto.agent.AgentRegisterRequest;
import com.matdongsan.api.dto.user.SocialLoginDto;
import com.matdongsan.api.dto.user.UserLoginDto;
import com.matdongsan.api.dto.user.UserSignupDto;
import com.matdongsan.api.service.AgentService;
import com.matdongsan.api.service.SocialAuthService;
import com.matdongsan.api.util.JwtUtil;
import com.matdongsan.api.vo.AgentVO;
import com.matdongsan.api.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    "role", user.getRole()
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

  // 회원 프로필 조회

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

  // 중개인 삭제

  // 중개인 리뷰 작성

  // 중개인 리뷰 조회

  // 중개인 리뷰 수정

  // 중개인 리뷰 삭제

  // 중개인 신고

  // 중개인 채팅
}
