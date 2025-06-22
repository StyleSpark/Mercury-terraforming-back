package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.agent.*;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.AgentService;
import com.matdongsan.api.vo.AgentMarkVO;
import com.matdongsan.api.vo.AgentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
@Tag(name = "중개인 API", description = "중개인 정보 및 리뷰 관리 API")
public class AgentController {

  private final AgentService service;

  @Operation(summary = "중개인 목록 조회", description = "주소, 매물명, 유형, 중개인 이름, 브랜드명을 기반으로 중개인 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<?> getAgents(@ParameterObject AgentGetRequest request) {
    Map<String, Object> response = service.getAgentListWithPagination(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(summary = "지도 범위 내 중개인 조회", description = "지도의 현재 뷰 영역(bounding box) 안에 존재하는 중개인 목록을 조회합니다.")
  @GetMapping("/withinBounds")
  public ResponseEntity<?> getAgentListWithinBounds(@ParameterObject AgentGetRequest request) {
    Map<String, Object> response = service.getAgentListWithinBounds(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(summary = "중개인 단일 조회", description = "중개인 ID를 통해 해당 중개인의 상세 정보를 조회합니다.")
  @GetMapping("/{agentId}")
  public ResponseEntity<?> getAgentDetail(
          @Parameter(description = "중개인 고유 ID", example = "1")
          @PathVariable Long agentId) {
    AgentVO agentDetail = service.getAgentDetail(agentId);
    return ResponseEntity.ok(ApiResponse.success(agentDetail));
  }

  @Operation(
          summary = "중개인 리뷰 작성",
          description = "특정 중개인에 대해 리뷰를 작성합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/{agentId}/reviews")
  public ResponseEntity<?> createReview(
          @Parameter(description = "중개인 고유 ID", example = "1") @PathVariable Long agentId,
          @RequestBody(required = true) AgentReviewCreateRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setUserId(user.getId());
    request.setAgentId(agentId);
    service.createReview(request);
    return ResponseEntity.ok(ApiResponse.success("중개인에 대한 리뷰가 작성되었습니다."));
  }

  @Operation(summary = "중개인 리뷰 목록 조회", description = "특정 중개인의 리뷰 목록을 페이지네이션 형식으로 조회합니다.")
  @GetMapping("/{agentId}/reviews")
  public ResponseEntity<?> getAgentReviews(
          @Parameter(description = "중개인 고유 ID", example = "1") @PathVariable Long agentId,
          @ParameterObject AgentReviewGetRequest request) {

    request.setAgentId(agentId);
    Map<String, Object> response = service.getAgentReviewListWithPagination(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(summary = "중개인 매물 리스트 조회", description = "특정 중개인이 등록한 매물 목록을 조회합니다.")
  @GetMapping("/{agentId}/properties")
  public ResponseEntity<?> getPropertiesByAgent(
          @Parameter(description = "중개인 고유 ID", example = "1") @PathVariable Long agentId,
          @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1") int page,
          @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {

    Map<String, Object> result = service.getPropertiesByAgent(agentId, page, size);
    return ResponseEntity.ok(ApiResponse.success(result));
  }

  @Operation(
          summary = "중개인 등록 신청",
          description = "일반 사용자가 중개인으로 전환 신청합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/register")
  public ResponseEntity<?> registerAgent(
          @Parameter(description = "중개인 등록 요청 JSON") @RequestPart("request") AgentRegisterRequest request,
          @Parameter(description = "프로필 이미지") @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) throws Exception {

    request.setUserId(user.getId());
    request.setProfileImage(profileImage);
    service.registerAgent(request);
    return ResponseEntity.ok(ApiResponse.success("중개인 등록 신청이 완료되었습니다."));
  }

  /**
   * 중개인 리뷰 수정
   * @param reviewId 중개인 리뷰 id
   * @param request 리뷰 내용, 점수
   * @return 리뷰 수정 성공 여부
   */
  @PatchMapping("/agents/reviews/{reviewId}")
  public ResponseEntity<?> updateAgentReview(@PathVariable Long reviewId, @RequestBody AgentReviewUpdateRequest request) {
    // TODO: 로그인 사용자 인증 구현 후 수정
    request.setReviewId(reviewId);
    request.setUserId(24L);
    service.updateAgentReview(request);
    return ResponseEntity.ok(ApiResponse.success("중개인 리뷰가 수정되었습니다."));
  }


  /**
   * 중개인 리뷰 삭제
   * @param reviewId 중개인 리뷰 id
   * @return 중개인 리뷰 삭제 성공 여부
   */
  @DeleteMapping("/agents/reviews/{reviewId}")
  public ResponseEntity<?> deleteAgentReview(@PathVariable Long reviewId,@AuthenticationPrincipal UserRole user) {
    service.deleteAgentReview(reviewId, user.getId());
    return ResponseEntity.ok(ApiResponse.success("중개인 리뷰가 삭제되었습니다."));
  }


  /**
   * 중개인 삭제 (더 이상 '중개인'으로써 활동을 하고 싶지 않을 때라 가정, agents 테이블의 deleted_at 값 변경 / soft-delete)
   * @param request userId TODO: 로그인 사용자 인증 구현 후 수정
   * @return 중개인 삭제 성공 여부
   */
  @DeleteMapping("/my-page")
  public ResponseEntity<?> deleteAgent(@RequestBody AgentDeleteRequest request) {
    service.deleteAgent(request);
    return ResponseEntity.ok(ApiResponse.success("중개인 삭제가 완료되었습니다."));
  }

  /**
   * 중개인 수정 (이직, 사무소 이동, 자기소개 등)
   * @param request AgentUpdateRequest
   * @return 중개인 수정 성공 여부
   */
  @PatchMapping("/my-page")
  public ResponseEntity<?> updateAgent(@RequestBody AgentUpdateRequest request,@AuthenticationPrincipal UserRole user) {
    service.updateAgent(request, user.getId());
    return ResponseEntity.ok(ApiResponse.success("중개인 정보가 수정되었습니다."));
  }

  @Operation(summary = "지도 클러스터 마커 조회")
  @GetMapping("/markers")
  public ResponseEntity<?> getAgentMarkersWithinBounds(@ModelAttribute AgentBoundsRequest request) {
    List<AgentMarkVO> markers = service.getAgentMarkersWithinBounds(request);
    return ResponseEntity.ok(ApiResponse.success(markers));
  }


}
