package com.matdongsan.api.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReservationCreateDto {
  private Long id;
  private String orderId;
  private Long propertyId;
  private Long userId;
  private String info;
  private LocalDateTime time;
  private Long deposit;
  private String status;
}
