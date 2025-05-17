package com.matdongsan.api.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class ReservationCreateDto {
  private Long id;
  private String orderId;
  private Long propertyId;
  private Long userId;
  private String info;
  private LocalDate reservedDate;
  private LocalTime reservedTime;
  private Long deposit;
  private String status;
}
