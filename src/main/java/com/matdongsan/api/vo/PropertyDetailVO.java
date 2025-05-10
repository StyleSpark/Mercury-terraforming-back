package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class PropertyDetailVO {
  private String content;           // 상세 설명
  private LocalDate availableDate;  // 입주 가능일

  private Boolean hasElevator;
  private Boolean hasBed;
  private Boolean hasParking;

  private Boolean hasHeating;
  private Boolean hasCooling;
  private Boolean hasBath;
  private Boolean hasMicrowave;
  private Boolean hasBurner;
  private Boolean hasFridge;
  private Boolean hasShoeCloset;
  private Boolean hasTv;
  private Boolean hasCloset;
  private Boolean hasDiningTable;
  private Boolean hasTableItem;
  private Boolean hasWasher;
  private Boolean hasInduction;
  private Boolean hasPet;

  private LocalDateTime createdAt;
}
