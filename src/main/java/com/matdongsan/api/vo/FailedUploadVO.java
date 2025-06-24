package com.matdongsan.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FailedUploadVO {
  private Long id;
  private Long propertyId;
  private String key;
  private String url;
  private String contentType;
  private byte[] fileData;
  private int retryCount;
  private LocalDateTime createdAt;
  private LocalDateTime lastRetryAt;
}

