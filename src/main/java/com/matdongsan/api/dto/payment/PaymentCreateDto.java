package com.matdongsan.api.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentCreateDto {

  @Schema(description = "결제 내역 ID", example = "101")
  private Long id;

  @Schema(description = "주문 ID", example = "order_abc123")
  private String orderId;

  @Schema(description = "결제한 사용자 ID", example = "42")
  private Long userId;

  @Schema(description = "결제 키 (PG사에서 발급)", example = "paykey_9876abcd")
  private String paymentKey;

  @Schema(description = "결제된 상품 이름", example = "예약 이용권 5회")
  private String itemName;

  @Schema(description = "결제 금액", example = "7500")
  private Long amount;

  @Schema(description = "결제 상태", example = "DONE")
  private String status;
}
