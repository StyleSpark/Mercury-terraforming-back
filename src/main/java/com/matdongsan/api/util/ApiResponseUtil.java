package com.matdongsan.api.util;

import com.matdongsan.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {

  public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
    return ResponseEntity.ok(ApiResponse.success(data));
  }

  public static ResponseEntity<ApiResponse<String>> okMessage(String message) {
    return ResponseEntity.ok(ApiResponse.success(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> custom(int status, String message, T data) {
    return ResponseEntity.status(status).body(ApiResponse.success(status, message, data));
  }
}


