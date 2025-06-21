package com.matdongsan.api.dto.property;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PropertyCreateRequest {

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "매물 제목", example = "깔끔한 원룸 풀옵션")
  private String title;

  @Schema(description = "카테고리", example = "원룸")
  private String category;

  @Schema(description = "가격", example = "100000000")
  private Long price;

  @Schema(description = "보증금", example = "5000000")
  private Long deposit;

  @Schema(description = "월세", example = "500000")
  private Long rentalFee;

  @Schema(description = "층 정보 (예: 저층, 중층, 고층)", example = "고층")
  private String floorType;

  @Schema(description = "층 수", example = "5")
  private Integer floor;

  @Schema(description = "면적", example = "22.5")
  private BigDecimal roomSize;

  @Schema(description = "관리비", example = "70000")
  private Integer maintenanceFee;

  @Schema(description = "썸네일 이미지 URL", example = "/img/example.jpg")
  private String thumbnailUrl;

  @Schema(description = "상태 (ACTIVE, DELETED)", example = "ACTIVE")
  private String status;

  @Schema(description = "주소", example = "서울시 강남구 논현동")
  private String address;

  @Schema(description = "상세주소", example = "101-101호")
  private String addressDetail;

  @Schema(description = "우편번호", example = "06236")
  private String postcode;

  @Schema(description = "위도", example = "37.4999")
  private BigDecimal latitude;

  @Schema(description = "경도", example = "127.0378")
  private BigDecimal longitude;

  @Schema(description = "조회수", example = "0")
  private Long hitCount;

  @Schema(description = "매물 타입 ID", example = "1")
  private Integer propertyTypeId;

  @Schema(description = "태그 목록", example = "[\"풀옵션\", \"신축\"]")
  private List<String> tags;

  @Schema(description = "매물 종류", example = "월세")
  private String type;

  @Schema(description = "상세 정보 객체")
  private PropertyDetailRequest detail;

  @Schema(description = "대표 이미지 (썸네일)")
  private MultipartFile thumbnail;

  @Schema(description = "이미지 리스트")
  private List<MultipartFile> images;

  @Schema(description = "이미지 URL 리스트")
  private List<String> imageUrls = new ArrayList<>();
}
