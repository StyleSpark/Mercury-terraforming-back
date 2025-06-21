package com.matdongsan.api.dto.payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseTicketDto {

  @Schema(description = "구매 기록 ID", example = "1001")
  private Long id;

  @Schema(description = "주문 ID", example = "order_abcd1234")
  private String orderId;

  @Schema(description = "결제 키 (PG사에서 발급)", example = "paykey_xyz789")
  private String paymentKey;

  @Schema(description = "티켓 상품 ID", example = "ticket_pt_10")
  private String ticketId;

  @Schema(description = "결제 금액", example = "15000")
  private Integer amount;

  @Schema(description = "사용자 ID", example = "42")
  private Long userId;

  @Schema(description = "남은 이용 횟수", example = "10")
  private Integer remainCount;

  @Schema(description = "상품 이름", example = "방문 예약 10회권")
  private String itemName;
}
