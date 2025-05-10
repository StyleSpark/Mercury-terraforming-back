package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PropertyVO {
  private Long id;
  private String title;
  private String category;
  private Long price;
  private Long deposit;
  private Long rentalFee;
  private String floorType;
  private Integer floor;
  private BigDecimal roomSize;
  private Integer maintenanceFee;
  private String thumbnailUrl;
  private String status;
  private String address;
  private String addressDetail;
  private String postcode;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private Long hitCount;
  private Integer propertyTypeId;
  private Integer roomCount;
  private Integer bathroomCount;

  private List<String> imageUrls;
  private PropertyDetailVO detail;

  private String nickname;
  private String profile;
}
