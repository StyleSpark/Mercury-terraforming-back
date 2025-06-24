package com.matdongsan.api.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeferredUpload {
  private Long propertyId;
  private String key;
  private byte[] data;
  private String contentType;
  private String url;
  private String oldUrl;

  public static DeferredUpload forInsert(Long propertyId, String key, byte[] data, String contentType, String url) {
    return new DeferredUpload(propertyId, key, data, contentType, url, null);
  }

  public static DeferredUpload forUpdate(String key, byte[] data, String contentType, String url, String oldUrl) {
    return new DeferredUpload(null, key, data, contentType, url, oldUrl);
  }
}
