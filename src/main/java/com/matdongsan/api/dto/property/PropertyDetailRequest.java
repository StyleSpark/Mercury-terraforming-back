package com.matdongsan.api.dto.property;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PropertyDetailRequest {
  private Long id;
  private Long propertyId;

  private String content;           // 상세 설명
  private LocalDate availableDate;  // 입주 가능일

  private Boolean bath;
  private Boolean hasLift;
  private Boolean bed;
  private Boolean lot;

  private Boolean heating;
  private Boolean cooling;
  private Boolean microwave;
  private Boolean burner;
  private Boolean fridge;
  private Boolean shoeCloset;
  private Boolean tv;
  private Boolean closet;
  private Boolean diningTable;
  private Boolean tableItem;
  private Boolean washer;
  private Boolean induction;

  private LocalDateTime createdAt;  // 등록일시
}
