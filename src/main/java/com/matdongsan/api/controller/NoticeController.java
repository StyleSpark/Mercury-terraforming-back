package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.notice.*;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.NoticeService;
import com.matdongsan.api.util.ApiResponseUtil;
import com.matdongsan.api.vo.NoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "공지사항 API", description = "관리자 공지사항 등록/조회/수정/삭제 API")
public class NoticeController {

  private final NoticeService service;

  @PostMapping
  @Operation(summary = "공지사항 등록", description = "관리자가 공지사항을 등록합니다.", security = @SecurityRequirement(name = "JWT"))
  public ResponseEntity<?> createNotice(
          @RequestBody NoticeCreateRequest request,
          @AuthenticationPrincipal UserRole user) {

    validateAdmin(user);

    request.setAuthor(user.getRole());
    Long id = service.createNotice(request);
    return ApiResponseUtil.ok(ApiResponse.success( id));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.", security = @SecurityRequirement(name = "JWT"))
  public ResponseEntity<?> updateNotice(
          @PathVariable Long id,
          @RequestBody NoticeUpdateRequest request,
          @AuthenticationPrincipal UserRole user) {

    validateAdmin(user);

    request.setId(id);
    request.setAdmin(user.getRole());

    boolean updated = service.updateNotice(request);
    if (updated) {
      return ApiResponseUtil.okMessage("공지사항이 수정되었습니다.");
    }
    throw new IllegalStateException("공지사항이 존재하지 않거나 수정에 실패했습니다.");
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.", security = @SecurityRequirement(name = "JWT"))
  public ResponseEntity<?> deleteNotice(
          @PathVariable Long id,
          @AuthenticationPrincipal UserRole user) {

    validateAdmin(user);

    NoticeDeleteRequest request = new NoticeDeleteRequest();
    request.setId(id);
    request.setAdmin(user.getRole());

    boolean deleted = service.deleteNotice(request);
    if (deleted) {
      return ApiResponseUtil.okMessage("공지사항이 삭제되었습니다.");
    }
    throw new IllegalStateException("공지사항이 존재하지 않거나 삭제에 실패했습니다.");
  }

  @GetMapping("/{id}")
  @Operation(summary = "공지사항 상세 조회", description = "공지사항 ID로 상세 정보를 조회합니다.")
  public ResponseEntity<?> getNoticeDetail(@PathVariable Long id) {
    NoticeVO detail = service.getNoticeDetail(id);
    return ApiResponseUtil.ok(detail);
  }

  @GetMapping
  @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 페이지네이션 형식으로 조회합니다.")
  public ResponseEntity<?> getNotices(NoticeGetRequest request) {
    Map<String, Object> response = service.getNoticeListWithPagination(request);
    return ApiResponseUtil.ok(response);
  }

  private void validateAdmin(UserRole user) {
    if (!"ADMIN".equals(user.getRole())) {
      throw new IllegalStateException("권한이 없습니다.");
    }
  }
}
