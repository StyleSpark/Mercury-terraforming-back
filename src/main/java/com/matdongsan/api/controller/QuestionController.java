package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.inquiry.QuestionCreateDto;
import com.matdongsan.api.dto.inquiry.QuestionDeleteDto;
import com.matdongsan.api.dto.inquiry.QuestionUpdateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.QuestionService;
import com.matdongsan.api.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

  final private QuestionService service;

  // 문의작성(사용자)
  @PostMapping
  ResponseEntity<?> createQuestion(@AuthenticationPrincipal UserRole user,@RequestBody QuestionCreateDto request){
    request.setUserId(user.getId());
    request.setStatus("PROCESSING");
    return ResponseEntity.ok(ApiResponse.success(service.createQuestion(request)));
  }
  // 내 문의 내역 조회
  @GetMapping
  ResponseEntity<?> getQuestionsByUser(@AuthenticationPrincipal UserRole user){
    List<QuestionVO> response = service.getQuestionByUser(user.getId());
    return ResponseEntity.ok(ApiResponse.success(response));
  }

// 문의사항 상세 조회
  @GetMapping("/{id}")
  ResponseEntity<?> getQuestionDetail(@AuthenticationPrincipal UserRole user, @PathVariable Long id) {
    QuestionVO question = service.getQuestionById(id);

    // 추가 안전 처리 (null 체크 + 본인만 볼 수 있게)
//    if (question == null || !question.getId().equals(user.getId())) {
//      return ResponseEntity.badRequest().body(ApiResponse.fail("해당 문의를 조회할 수 없습니다."));
//    }

    return ResponseEntity.ok(ApiResponse.success(question));
  }

  // 문의 사항 수정
  @PatchMapping("/{id}")
  ResponseEntity<?> updateQuestion(@AuthenticationPrincipal UserRole user, @PathVariable Long id,@RequestBody QuestionUpdateDto request) {
    request.setId(id);
    request.setUserId(user.getId());
    return ResponseEntity.ok(ApiResponse.success(service.updateQuestionByUser(request)));
  }

  // 문의 사항 삭제
  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteQuestion(@AuthenticationPrincipal UserRole user,
                                   @PathVariable Long id) {
    QuestionDeleteDto request = new QuestionDeleteDto();
    request.setUserId(user.getId());
    request.setId(id);
    return ResponseEntity.ok(ApiResponse.success(service.deleteQuestionByUser(request)));
  }

  // 문의사항 답변 등록(운영자)

}
