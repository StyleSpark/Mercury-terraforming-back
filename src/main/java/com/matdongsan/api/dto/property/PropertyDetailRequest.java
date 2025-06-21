package com.matdongsan.api.dto.property;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "매물 상세 정보 요청 DTO")
public class PropertyDetailRequest {

  @Schema(description = "상세 정보 ID", example = "1")
  private Long id;

  @Schema(description = "매물 ID", example = "101")
  private Long propertyId;

  @Schema(description = "매물 상세 설명", example = "강남역 도보 5분, 넓고 조용한 원룸입니다.")
  private String content;

  @Schema(description = "입주 가능일", example = "2025-07-01")
  private LocalDate availableDate;

  @Schema(description = "엘리베이터 여부", example = "true")
  private Boolean hasElevator;

  @Schema(description = "침대 포함 여부", example = "true")
  private Boolean hasBed;

  @Schema(description = "주차 가능 여부", example = "true")
  private Boolean hasParking;

  @Schema(description = "난방 여부", example = "true")
  private Boolean hasHeating;

  @Schema(description = "냉방 여부", example = "true")
  private Boolean hasCooling;

  @Schema(description = "욕실 여부", example = "true")
  private Boolean hasBath;

  @Schema(description = "전자레인지 포함 여부", example = "true")
  private Boolean hasMicrowave;

  @Schema(description = "가스레인지/버너 포함 여부", example = "true")
  private Boolean hasBurner;

  @Schema(description = "냉장고 포함 여부", example = "true")
  private Boolean hasFridge;

  @Schema(description = "신발장 포함 여부", example = "true")
  private Boolean hasShoeCloset;

  @Schema(description = "TV 포함 여부", example = "true")
  private Boolean hasTv;

  @Schema(description = "옷장 포함 여부", example = "true")
  private Boolean hasCloset;

  @Schema(description = "식탁 포함 여부", example = "true")
  private Boolean hasDiningTable;

  @Schema(description = "테이블 소품 포함 여부", example = "true")
  private Boolean hasTableItem;

  @Schema(description = "세탁기 포함 여부", example = "true")
  private Boolean hasWasher;

  @Schema(description = "인덕션 포함 여부", example = "true")
  private Boolean hasInduction;

  @Schema(description = "반려동물 허용 여부", example = "false")
  private Boolean hasPet;

  @Schema(description = "상세정보 등록일", example = "2025-06-20T15:30:00")
  private LocalDateTime createdAt;
}
