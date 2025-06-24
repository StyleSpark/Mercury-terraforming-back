package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.payment.PurchaseTicketDto;
import com.matdongsan.api.dto.reservation.ReservationConfirmDto;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.security.UserRole;
import com.matdongsan.api.service.PaymentService;
import com.matdongsan.api.vo.TempReservationVO;
import com.matdongsan.api.vo.TicketVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "결제 API", description = "대리 서비스 결제, 등록권 구매, 예약 기능을 제공합니다.")
public class PaymentController {

  private final PaymentService service;

  @Operation(
          summary = "임시 예약 생성",
          description = "예약을 진행하기 전 임시로 데이터를 저장합니다. 실제 예약 확정 전단계입니다."
  )
  @PostMapping("/temp")
  public ResponseEntity<?> createTempReservationConfirm(
          @RequestBody ReservationCreateDto request) {

    TempReservationVO response = service.createTempReservation(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, response));
  }

  @Operation(
          summary = "예약 확정 및 결제 완료 처리",
          description = "결제가 완료되면 임시 예약을 확정하고 예약 ID를 반환합니다. (비관적 락으로 동시성 제어)"
  )
  @PostMapping("/reservationConfirm")
  public ResponseEntity<?> confirmReservation(
          @RequestBody ReservationConfirmDto request) {

    Long reservationId = service.confirmReservation(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, reservationId));
  }

  @Operation(
          summary = "등록권 또는 티켓 구매",
          description = "회원이 등록권 또는 티켓을 구매합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping("/purchaseTicket")
  public ResponseEntity<?> purchaseTicket(
          @RequestBody PurchaseTicketDto request,
          @Parameter(hidden = true) @AuthenticationPrincipal UserRole user) {

    request.setUserId(user.getId());
    Long purchaseId = service.purchaseTicket(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, purchaseId));
  }

  @Operation(
          summary = "티켓 정보 조회",
          description = "사용자가 보유한 등록권 또는 티켓 정보를 조회합니다."
  )
  @GetMapping("/tickets")
  public ResponseEntity<?> getTickets(
          @Parameter(description = "조회할 티켓 ID (null이면 전체 조회)", example = "ticket_abc123")
          @RequestParam(required = false) String ticketId) {

    List<TicketVO> tickets = service.getTicketInfoData(ticketId);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(201, tickets));
  }

  // TODO: 등록권 존재 여부 체크 → 구매 중복 방지
}
