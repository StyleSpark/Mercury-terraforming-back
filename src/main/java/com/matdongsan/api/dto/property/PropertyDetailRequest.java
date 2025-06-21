package com.matdongsan.api.dto.property;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PropertyDetailRequest {

  @Schema(description = "상세 설명", example = "엘리베이터, 냉장고, 침대 포함된 깔끔한 원룸입니다.")
  private String content;

  @Schema(description = "입주 가능일", example = "2025-07-01")
  private LocalDate availableDate;

  @Schema(description = "엘리베이터 유무", example = "true")
  private Boolean hasElevator;

  @Schema(description = "침대 포함 여부", example = "true")
  private Boolean hasBed;

  @Schema(description = "주차 가능 여부", example = "false")
  private Boolean hasParking;

  @Schema(description = "난방 유무", example = "true")
  private Boolean hasHeating;

  @Schema(description = "에어컨 유무", example = "true")
  private Boolean hasCooling;

  @Schema(description = "욕실 있음", example = "true")
  private Boolean hasBath;

  @Schema(description = "전자레인지 있음", example = "false")
  private Boolean hasMicrowave;

  @Schema(description = "가스레인지 있음", example = "true")
  private Boolean hasBurner;

  @Schema(description = "냉장고 있음", example = "true")
  private Boolean hasFridge;

  @Schema(description = "신발장 있음", example = "true")
  private Boolean hasShoeCloset;

  @Schema(description = "TV 있음", example = "true")
  private Boolean hasTv;

  @Schema(description = "옷장 있음", example = "true")
  private Boolean hasCloset;

  @Schema(description = "식탁 있음", example = "false")
  private Boolean hasDiningTable;

  @Schema(description = "테이블 있음", example = "false")
  private Boolean hasTableItem;

  @Schema(description = "세탁기 있음", example = "true")
  private Boolean hasWasher;

  @Schema(description = "인덕션 있음", example = "false")
  private Boolean hasInduction;

  @Schema(description = "반려동물 가능", example = "true")
  private Boolean hasPet;

  @Schema(description = "생성일시", example = "2025-06-20T12:00:00")
  private LocalDateTime createdAt;
}
