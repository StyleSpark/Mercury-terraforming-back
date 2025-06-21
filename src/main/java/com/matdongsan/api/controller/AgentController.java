package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.agent.*;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.AgentService;
import com.matdongsan.api.util.ApiResponseUtil;
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
  public ResponseEntity<?> getAgentsWithinBounds(@ModelAttribute AgentBoundsRequest request) {
    List<AgentGetResponse> agents = service.getAgentsWithinBounds(request);
    return ResponseEntity.ok(ApiResponse.success(agents));
  }

  @Operation(summary = "중개인 단일 조회", description = "중개인 ID를 통해 해당 중개인의 상세 정보를 조회합니다.")
  @GetMapping("/{agentId}")
  public ResponseEntity<?> getAgentDetail(
          @Parameter(description = "중개인 고유 ID", example = "1")
          @PathVariable Long agentId) {
    AgentVO agentDetail = service.getAgentDetail(agentId);
    return ApiResponseUtil.ok(agentDetail);
  }

  @Operation(summary = "중개인 매물 리스트 조회", description = "특정 중개인이 등록한 매물 목록을 조회합니다.")
  @GetMapping("/{agentId}/properties")
  public ResponseEntity<?> getPropertiesByAgent(
          @Parameter(description = "중개인 고유 ID", example = "1") @PathVariable Long agentId,
          @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1") int page,
          @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {

    Map<String, Object> result = service.getPropertiesByAgent(agentId, page, size);
    return  ApiResponseUtil.ok(result);
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
    return  ApiResponseUtil.ok("중개인 등록 신청이 완료되었습니다.");
  }

  /**
   * 중개인 삭제 (더 이상 '중개인'으로써 활동을 하고 싶지 않을 때라 가정, agents 테이블의 deleted_at 값 변경 / soft-delete)
   * @param request userId
   * @return 중개인 삭제 성공 여부
   */
  @DeleteMapping("/me")
  public ResponseEntity<?> deleteAgent(@RequestBody AgentDeleteRequest request) {
    service.deleteAgent(request);
    return  ApiResponseUtil.ok("중개인 삭제가 완료되었습니다.");
  }

  /**
   * 중개인 수정 (이직, 사무소 이동, 자기소개 등)
   * @param request AgentUpdateRequest
   * @return 중개인 수정 성공 여부
   */
  @PatchMapping("/me")
  public ResponseEntity<?> updateAgent(@RequestBody AgentUpdateRequest request,@AuthenticationPrincipal UserRole user) {
   service.updateAgent(request, user.getId());
    return  ApiResponseUtil.ok("중개인 정보가 수정되었습니다.");
  }

}
