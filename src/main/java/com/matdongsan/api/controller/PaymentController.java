package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.payment.PurchaseTicketDto;
import com.matdongsan.api.dto.reservation.ReservationConfirmDto;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.PaymentService;
import com.matdongsan.api.vo.TempReservationVO;
import com.matdongsan.api.vo.TicketVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  /**
   * 등록권 및 티켓 구매
   * @param request
   * @param user
   * @return
   */
  @PostMapping("/purchaseTicket")
  public ResponseEntity<?> purchaseTicket(@RequestBody PurchaseTicketDto request, @AuthenticationPrincipal UserRole user) {
    request.setUserId(user.getId());
    Long purchaseId = service.purchaseTicket(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, purchaseId));
  }

  /**
   * 티켓 데이터 조회
   * @param ticketId
   * @return
   */
  @GetMapping("/tickets")
  public ResponseEntity<?> getTickets(@RequestParam(required = false) String ticketId) {
    List<TicketVO> tickets = service.getTicketInfoData(ticketId);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, tickets));
  }
  /**
   *  Todo: 등록권 체크 하는 로직 추가하여 등록권이 없는 경우에만 다시 구매하도록 막아야함
   */
  
}
