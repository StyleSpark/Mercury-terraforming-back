package com.matdongsan.api.dto.property;

import io.swagger.v3.oas.annotations.media.Schema;
import com.matdongsan.api.vo.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PropertyUpdateRequest {

  @Schema(description = "매물 ID", example = "123")
  private Long id;

  @Schema(description = "사용자 ID", example = "5")
  private Long userId;

  @Schema(description = "매물 제목", example = "신축 오피스텔")
  private String title;

  @Schema(description = "카테고리", example = "오피스텔")
  private String category;

  @Schema(description = "매매가", example = "300000000")
  private Long price;

  @Schema(description = "보증금", example = "5000000")
  private Long deposit;

  @Schema(description = "월세", example = "700000")
  private Long rentalFee;

  @Schema(description = "층 유형", example = "고층")
  private String floorType;

  @Schema(description = "해당 층수", example = "8")
  private Integer floor;

  @Schema(description = "면적 (㎡)", example = "23.5")
  private BigDecimal roomSize;

  @Schema(description = "관리비", example = "70000")
  private Integer maintenanceFee;

  @Schema(description = "썸네일 이미지 URL", example = "/uploads/img_thumbnail.jpg")
  private String thumbnailUrl;

  @Schema(description = "매물 상태", example = "ACTIVE")
  private String status;

  @Schema(description = "주소", example = "서울특별시 서초구 서초대로 77길")
  private String address;

  @Schema(description = "상세 주소", example = "101동 102호")
  private String addressDetail;

  @Schema(description = "우편번호", example = "06611")
  private String postcode;

  @Schema(description = "위도", example = "37.123456")
  private BigDecimal latitude;

  @Schema(description = "경도", example = "127.654321")
  private BigDecimal longitude;

  @Schema(description = "조회 수", example = "120")
  private Long hitCount;

  @Schema(description = "매물 유형 ID (문자열)", example = "APT")
  private String propertyTypeId;

  @Schema(description = "매물 상세 정보 객체")
  private PropertyDetailRequest detail;

  @Schema(description = "기존 이미지 URL 리스트", example = "[\"/uploads/1.jpg\", \"/uploads/2.jpg\"]")
  private List<String> imageUrls;

  @Schema(description = "태그 객체 리스트", example = "[{\"id\":1,\"name\":\"풀옵션\"}]")
  private List<Tag> tags;

  @Schema(description = "새 썸네일 이미지 파일")
  private MultipartFile thumbnail;

  @Schema(description = "이미지 파일 리스트")
  private List<MultipartFile> images;
}
