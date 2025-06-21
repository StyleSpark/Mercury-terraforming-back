package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.agent.AgentReviewCreateRequest;
import com.matdongsan.api.dto.agent.AgentReviewGetRequest;
import com.matdongsan.api.dto.agent.AgentReviewUpdateRequest;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.AgentService;
import com.matdongsan.api.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agent-reviews")
@RequiredArgsConstructor
@Tag(name = "중개인 리뷰 API", description = "중개인 리뷰 관리")
public class AgentReviewController {

  private final AgentService service;

  @Operation(
          summary = "중개인 리뷰 작성",
          description = "특정 중개인에 대해 리뷰를 작성합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/{agentId}")
  public ResponseEntity<?> createReview(
          @Parameter(description = "중개인 고유 ID", example = "1") @PathVariable Long agentId,
          @RequestBody(required = true) AgentReviewCreateRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setUserId(user.getId());
    request.setAgentId(agentId);
    service.createReview(request);
    return ApiResponseUtil.ok("중개인에 대한 리뷰가 작성되었습니다.");
  }

  @Operation(summary = "중개인 리뷰 목록 조회", description = "특정 중개인의 리뷰 목록을 페이지네이션 형식으로 조회합니다.")
  @GetMapping("/{agentId}")
  public ResponseEntity<?> getAgentReviews(
          @Parameter(description = "중개인 고유 ID", example = "1") @PathVariable Long agentId,
          @ParameterObject AgentReviewGetRequest request) {

    request.setAgentId(agentId);
    Map<String, Object> response = service.getAgentReviewListWithPagination(request);
    return ApiResponseUtil.ok(response);
  }
  /**
   * 중개인 리뷰 수정
   * @param reviewId 중개인 리뷰 id
   * @param request 리뷰 내용, 점수
   * @return 리뷰 수정 성공 여부
   */
  @PatchMapping("/{reviewId}")
  public ResponseEntity<?> updateAgentReview(@PathVariable Long reviewId, @RequestBody AgentReviewUpdateRequest request,@AuthenticationPrincipal UserRole user) {
    request.setReviewId(reviewId);
    request.setUserId(user.getId());
    service.updateAgentReview(request);
    return ApiResponseUtil.ok("중개인 리뷰가 수정되었습니다.");
  }


  /**
   * 중개인 리뷰 삭제
   * @param reviewId 중개인 리뷰 id
   * @return 중개인 리뷰 삭제 성공 여부
   */
  @DeleteMapping("/{reviewId}")
  public ResponseEntity<?> deleteAgentReview(@PathVariable Long reviewId,@AuthenticationPrincipal UserRole user) {
    service.deleteAgentReview(reviewId, user.getId());
    return ApiResponseUtil.ok("중개인 리뷰가 삭제되었습니다.");
  }

}
