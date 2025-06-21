package com.matdongsan.api.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class ReservationCreateDto {

  @Schema(description = "예약 ID (임시 생성용으로 nullable)", example = "null")
  private Long id;

  @Schema(description = "주문 ID (결제 식별용)", example = "order_20240621_123456")
  private String orderId;

  @Schema(description = "예약 대상 매물 ID", example = "101")
  private Long propertyId;

  @Schema(description = "예약자(사용자) ID", example = "42")
  private Long userId;

  @Schema(description = "방문 목적 또는 참고 정보", example = "조용한 환경 확인 목적")
  private String info;

  @Schema(description = "예약 일자", example = "2025-06-22")
  private LocalDate reservedDate;

  @Schema(description = "예약 시간", example = "14:30")
  private LocalTime reservedTime;

  @Schema(description = "예약 예치금 (단위: 원)", example = "15000")
  private Long deposit;

  @Schema(description = "예약 상태", example = "TEMPORARY / CONFIRMED / CANCELED")
  private String status;
}
