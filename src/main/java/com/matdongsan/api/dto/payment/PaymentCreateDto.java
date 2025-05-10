package com.matdongsan.api.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentCreateDto {
  private Long id;
  private String orderId;
  private Long userId;
  private String paymentKey;
  private String itemName;
  private Long amount;
  private String status;
}
