package com.matdongsan.api.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Alias("agent")
public class AgentVO {
  private Long id;
  private Long userId;
  private String agentName;
  private String brand;
  private String address;
  private String addressDetail;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private String profileUrl;
  private LocalDate createdAt;
  private String licenseNumber;
  private BigDecimal reviewAvg;
  private Integer reviewCount;
}
