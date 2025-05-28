package com.matdongsan.api.dto.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PropertyCreateRequest {
  private Long id;
  private Long userId;
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
  private List<String> tags;

  private MultipartFile thumbnail;

  //detail
  private PropertyDetailRequest detail;

  private List<MultipartFile> images;
  private List<String> imageUrls = new ArrayList<>();
}
