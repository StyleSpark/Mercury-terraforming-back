package com.matdongsan.api.dto.property;

import com.matdongsan.api.vo.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class PropertyUpdateRequest {
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
  private String propertyTypeId; // 문자열로 변경
  private PropertyDetailRequest detail;
  private List<String> imageUrls;

  // 🔥 반드시 추가
  private List<Tag> tags;

  private MultipartFile thumbnail;
  private List<MultipartFile> images;
}