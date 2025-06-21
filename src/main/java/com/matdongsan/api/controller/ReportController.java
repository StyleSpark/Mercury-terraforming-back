package com.matdongsan.api.controller;

import com.matdongsan.api.dto.agent.AgentReportCreateRequest;
import com.matdongsan.api.dto.agent.AgentReportGetRequest;
import com.matdongsan.api.dto.report.ReportCreateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.ReportService;
import com.matdongsan.api.util.ApiResponseUtil;
import com.matdongsan.api.vo.ReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "신고 API", description = "매물 및 중개인 신고 등록 및 조회 기능을 제공합니다.")
public class ReportController {

  private final ReportService service;

  @Operation(
          summary = "매물 신고 등록",
          description = "사용자가 매물을 신고합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping
  public ResponseEntity<?> createReport(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @RequestBody ReportCreateDto request) {

    request.setUserId(user.getId());
    Long reportId = service.createReport(request);
    return ApiResponseUtil.ok(reportId);
  }

  @Operation(
          summary = "내가 신고한 매물 목록 조회",
          description = "로그인한 사용자가 본인이 신고한 매물 신고 내역을 조회합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @GetMapping
  public ResponseEntity<?> getMyReports(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    List<ReportVO> reports = service.getReportsByUser(user.getId());
    return ApiResponseUtil.ok(reports);
  }

  @Operation(
          summary = "중개인 신고 등록",
          description = "사용자가 특정 중개인을 신고합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/agents/{agentId}/reports")
  public ResponseEntity<?> createAgentReport(
          @PathVariable Long agentId,
          @RequestBody AgentReportCreateRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setUserId(user.getId());
    request.setTargetId(agentId);
    service.createAgentReport(request);
    return ApiResponseUtil.okMessage("중개인 신고가 완료되었습니다.");
  }

  @Operation(
          summary = "내가 신고한 중개인 목록 조회",
          description = "로그인한 사용자가 신고한 중개인 목록을 조회합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @GetMapping("/agents/reports/me")
  public ResponseEntity<?> getAgentReports(
          AgentReportGetRequest request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setUserId(user.getId());
    Map<String, Object> response = service.getAgentReports(request);
    return ApiResponseUtil.ok(response);
  }

  // TODO: 특정 중개인에 대한 전체 신고 목록 (관리자 전용) 추후 구현 예정
}
