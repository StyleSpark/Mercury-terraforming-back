package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.inquiry.QuestionCreateDto;
import com.matdongsan.api.dto.inquiry.QuestionDeleteDto;
import com.matdongsan.api.dto.inquiry.QuestionUpdateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.QuestionService;
import com.matdongsan.api.vo.QuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
@Tag(name = "문의사항 API", description = "사용자 문의 등록, 수정, 삭제, 조회 기능을 제공합니다.")
public class QuestionController {

  private final QuestionService service;

  @Operation(
          summary = "문의 작성",
          description = "사용자가 1:1 문의를 작성합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping
  public ResponseEntity<?> createQuestion(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @RequestBody QuestionCreateDto request) {

    request.setUserId(user.getId());
    request.setStatus("PROCESSING");
    return ResponseEntity.ok(ApiResponse.success(service.createQuestion(request)));
  }

  @Operation(
          summary = "내 문의 내역 조회",
          description = "사용자가 본인의 문의 내역을 조회합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @GetMapping
  public ResponseEntity<?> getQuestionsByUser(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    List<QuestionVO> response = service.getQuestionByUser(user.getId());
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(
          summary = "문의 상세 조회",
          description = "문의 ID를 통해 상세 내역을 조회합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @GetMapping("/{id}")
  public ResponseEntity<?> getQuestionDetail(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @Parameter(description = "문의 ID", example = "101") @PathVariable Long id) {

    QuestionVO question = service.getQuestionById(id);

    // TODO: 권한 확인 및 예외처리 (본인 확인)
    return ResponseEntity.ok(ApiResponse.success(question));
  }

  @Operation(
          summary = "문의 수정",
          description = "사용자가 작성한 문의를 수정합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PatchMapping("/{id}")
  public ResponseEntity<?> updateQuestion(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @Parameter(description = "문의 ID", example = "101") @PathVariable Long id,
          @RequestBody QuestionUpdateDto request) {

    request.setId(id);
    request.setUserId(user.getId());
    return ResponseEntity.ok(ApiResponse.success(service.updateQuestionByUser(request)));
  }

  @Operation(
          summary = "문의 삭제",
          description = "사용자가 작성한 문의를 삭제합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestion(
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user,
          @Parameter(description = "문의 ID", example = "101") @PathVariable Long id) {

    QuestionDeleteDto request = new QuestionDeleteDto();
    request.setUserId(user.getId());
    request.setId(id);
    return ResponseEntity.ok(ApiResponse.success(service.deleteQuestionByUser(request)));
  }

  // TODO: 운영자 답변 등록 기능 추가 예정
}
