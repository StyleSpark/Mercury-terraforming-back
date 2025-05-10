package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TempReservationVO {
  private Long tempId;
  private String orderId;
  private Long propertyId;
  private Long userId;
  private String info;
  private LocalDateTime time;
  private Long deposit;
}
