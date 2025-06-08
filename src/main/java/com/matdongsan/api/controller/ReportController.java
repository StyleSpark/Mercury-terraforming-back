package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.report.ReportCreateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.ReportService;
import com.matdongsan.api.vo.ReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

  final private ReportService service;

  /**
   * 매물 신고
   * @param user
   * @param request
   * @return
   */
  @PostMapping
  public ResponseEntity<?> createReport(@AuthenticationPrincipal UserRole user,
                                        @RequestBody ReportCreateDto request) {
    request.setUserId(user.getId()); // 신고자는 현재 로그인한 사용자
    Long reportId = service.createReport(request);
    return ResponseEntity.ok(ApiResponse.success(reportId));
  }

  /**
   * 매물 신고 조회
   * @param user
   * @return
   */
  @GetMapping
  public ResponseEntity<?> getMyReports(@AuthenticationPrincipal UserRole user) {
    List<ReportVO> response = service.getReportsByUser(user.getId());
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
