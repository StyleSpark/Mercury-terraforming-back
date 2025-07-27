package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.dto.reservation.ReservationGetDto;
import com.matdongsan.api.dto.reservation.ReservationTimeGetDto;
import com.matdongsan.api.service.ReservationService;
import com.matdongsan.api.vo.ReservationVO;
import com.matdongsan.api.vo.ReservedTimeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@Tag(name = "예약 API", description = "쇼잉 예약 등록 및 조회 API")
public class ReservationController {

  private final ReservationService service;

  @Operation(
          summary = "매물 쇼잉 예약 신청",
          description = "사용자가 특정 매물에 대해 쇼잉 예약을 신청합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @PostMapping
  public ResponseEntity<?> createReservation(
          @RequestBody ReservationCreateDto request) {

    Long id = service.createReservation(request);
    return ResponseEntity.ok(ApiResponse.success(id));
  }

  @Operation(
          summary = "사용자 예약 목록 조회",
          description = "로그인한 사용자의 예약 내역을 조회합니다. JWT 인증 필요.",
          security = @SecurityRequirement(name = "JWT")
  )
  @GetMapping
  public ResponseEntity<?> getReservationByUser(
          @Parameter(hidden = true) ReservationGetDto request) {

    List<ReservationVO> response = service.getReservations(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Operation(
          summary = "매물 예약된 시간 조회",
          description = "선택한 날짜의 특정 매물에 대해 이미 예약된 시간을 조회합니다."
  )
  @GetMapping("/times")
  public ResponseEntity<?> getReservedTimes(
          @Parameter(hidden = true) ReservationTimeGetDto request) {

    List<ReservedTimeVO> response = service.getReservationTimes(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  // TODO: 예약 상세조회, 수정, 취소 기능 추가 예정
  @Operation(summary = "매물 등록자 알림 조회", description = "등록자가 본인의 새 알림을 조회합니다.")
  @GetMapping("/notifications")
  public ResponseEntity<?> getNotifications(@RequestParam Long agentId) {
    List<ReservationVO> notifications = service.getNotifications(agentId);
    return ResponseEntity.ok(ApiResponse.success(notifications));
  }
}
