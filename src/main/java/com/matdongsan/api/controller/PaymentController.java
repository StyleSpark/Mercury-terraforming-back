package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.reservation.ReservationConfirmDto;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.service.PaymentService;
import com.matdongsan.api.vo.TempReservationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  final private PaymentService service;

  // 대리 서비스 결제 완료

  /**
   * 임시 예약 확인 생성
   * TODO: 임시 테이블 자동 삭제 로직 추가 해야함
   * @param request
   * @return
   */
  @PostMapping("/temp")
  public ResponseEntity<?> createTempReservationConfirm(@RequestBody ReservationCreateDto request) {
    TempReservationVO response = service.createTempReservation(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, response));
  }

  /**
   * 결제 완료 및 예약 확인 
   * TODO: 동시성 제어 기능 추가 및 코드 정리
   * @param request
   * @return
   */
  @PostMapping("/reservationConfirm")
  public ResponseEntity<?> confirmReservation(@RequestBody ReservationConfirmDto request) {
    Long reservationId = service.confirmReservation(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, reservationId));
  }
  // 등록권 결제 완료
}
