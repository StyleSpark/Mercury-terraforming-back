package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TempReservationVO {
  private Long tempId;
  private String orderId;
  private Long propertyId;
  private Long userId;
  private String info;
  private LocalDate reservedDate;
  private LocalTime reservedTime;
  private Long deposit;
}
