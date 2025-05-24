package com.matdongsan.api.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseTicketDto {
  private Long id;
  private String orderId;
  private String paymentKey;
  private String ticketId;
  private Integer amount;
  private Long userId;
  private Integer remainCount;
  private String itemName;
}
