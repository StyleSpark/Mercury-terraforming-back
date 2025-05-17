package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationVO {
  private Long id;
  private Long userId;
  private Long propertyId;
  private LocalTime reservedTime;
  private LocalDate reservedDate;
  private Long deposit;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
