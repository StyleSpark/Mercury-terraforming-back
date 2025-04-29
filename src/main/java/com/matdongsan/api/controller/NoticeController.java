package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.notice.NoticeRequest;
import com.matdongsan.api.service.NoticeService;
import com.matdongsan.api.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {
  private final NoticeService service;

  // 공지사항 등록

  // 공지사항 리스트 등록

  // 공지사항 상세 조회

  // 공지사항 조회

  /**
   * 공지사항 조희
   * @param request 특정 공지사항 검색 매개변수
   * @return 공지사항 리스트
   */
  @GetMapping
  public ResponseEntity<?> getNotices(NoticeRequest request) {
    List<NoticeVO> notices = service.getNoticeList(request);
    return ResponseEntity.ok(ApiResponse.success(notices));
  }

}
