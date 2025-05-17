package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.dto.reservation.ReservationGetDto;
import com.matdongsan.api.dto.reservation.ReservationTimeGetDto;
import com.matdongsan.api.service.ReservationService;
import com.matdongsan.api.vo.ReservationVO;
import com.matdongsan.api.vo.ReservedTimeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationService service;

  /**
   * 매물 쇼잉 예약 신청
   * @param request 예약 생성 dto
   * @return
   */
  @PostMapping
  public ResponseEntity<?> createReservation(@RequestBody ReservationCreateDto request){
    Long id = service.createReservation(request);
    return ResponseEntity.ok(ApiResponse.success(id));
  }

  /**
   * 쇼잉 예약 조회
   * @param request
   * @return
   */
  @GetMapping
  public ResponseEntity<?> getReservationByUser(ReservationGetDto request){
    List<ReservationVO> response = service.getReservations(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 매물 쇼잉 예약 날짜에 따라 
   * 예약된 시간 조회
   * @param request
   * @return
   */
  @GetMapping("/times")
  public ResponseEntity<?> getReservedTimes(ReservationTimeGetDto request){
    List<ReservedTimeVO> response = service.getReservationTimes(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
  //예약 상세

  //예약 수정
  
  //예약 취소
}
