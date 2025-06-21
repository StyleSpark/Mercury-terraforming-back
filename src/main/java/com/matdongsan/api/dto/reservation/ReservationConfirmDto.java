package com.matdongsan.api.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationConfirmDto {

  @Schema(description = "결제 주문 ID (orderId)", example = "order_20240621_123456")
  private String orderId;

  @Schema(description = "결제 승인 키 (PG사에서 반환된 paymentKey)", example = "pay_123abc456def")
  private String paymentKey;

  @Schema(description = "결제 금액 (단위: 원)", example = "15000")
  private Long amount;
}
