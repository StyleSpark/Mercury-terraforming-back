package com.matdongsan.api.dto.reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationConfirmDto {
  private String orderId;
  private String paymentKey;
  private Long amount;
}

