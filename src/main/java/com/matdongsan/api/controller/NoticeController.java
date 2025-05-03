package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.notice.NoticeCreateRequest;
import com.matdongsan.api.dto.notice.NoticeDeleteRequest;
import com.matdongsan.api.dto.notice.NoticeGetRequest;
import com.matdongsan.api.dto.notice.NoticeUpdateRequest;
import com.matdongsan.api.service.NoticeService;
import com.matdongsan.api.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {
  private final NoticeService service;

  // 공지사항 수정 (관리자)

  /**
   * 공지사항 수정 (관리자)
   * @param id 해당 공지사항 id
   * @param request 공지사항 데이터
   * @return 수정 결과
   */
  @PatchMapping("/{id}")
  public ResponseEntity<?> updateNotice(@PathVariable Long id, @RequestBody NoticeUpdateRequest request) {
    request.setId(id);
    String admin = "This is temp admin data"; // 실제 로그인 사용자
    request.setAdmin(admin);

    boolean result = service.updateNotice(request);
    // 이렇게 처리를 할지 고민중
    if (result) {
      return ResponseEntity.ok(ApiResponse.success(200, "공지사항이 수정되었습니다."));
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("수정 권한이 없습니다."));
  }

  /**
   * 공지사항 삭제
   * @param id 해당 공지사항 id
   * @return 삭제 결과
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteNotice(@PathVariable Long id) {
    NoticeDeleteRequest request = new NoticeDeleteRequest();
    request.setId(id);
    String admin = "This is temp admin data";
    request.setAdmin(admin);

    boolean result = service.deleteNotice(request);
    // boolean 값에 따라 리턴 방식 처리를 할지 고민중
    if (result) {
      return ResponseEntity.ok(ApiResponse.success(200, "공지사항이 삭제되었습니다."));
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("삭제 권한이 없습니다."));
  }

  /**
   * 공지사항 등록 (관리자용)
   * @param request 등록할 공지사항 데이터
   * @return 등록 결과
   */
  @PostMapping
  public ResponseEntity<?> createNotice(@RequestBody NoticeCreateRequest request) {
    String admin = "This is temp admin data"; // 현재 로그인 한 유저 정보
    // 관리자가 맞는지 확인후에 setter 적용
    // 그리고 service 실행 아니라면 fail - 요청 권한 부족과 같은 메시지 출력
    request.setAdmin(admin);
    Long id = service.createNotice(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, id));
  }

  // 공지사항 상세 조회

  /**
   * 공지사항 상세 조회
   * @param id 공지사항 id
   * @return 공지사항 상세 정보
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getNoticeDetail(@PathVariable Long id) {
    NoticeVO notices = service.getNoticeDetail(id);
    return ResponseEntity.ok(ApiResponse.success(notices));
  }

  // 공지사항 조회

  /**
   * 공지사항 조희
   *
   * @param request 특정 공지사항 검색 매개변수
   * @return 공지사항 리스트
   */
  @GetMapping
  public ResponseEntity<?> getNotices(NoticeGetRequest request) {
    List<NoticeVO> notices = service.getNoticeList(request);
    return ResponseEntity.ok(ApiResponse.success(notices));
  }

}
