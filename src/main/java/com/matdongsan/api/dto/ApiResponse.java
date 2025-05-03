package com.matdongsan.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
  private int status;
  private String message;
  private T data;

  private ApiResponse(int status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(200, "Success", data);
  }

  public static <T> ApiResponse<T> fail(String message) {
    return new ApiResponse<>(400, message, null);
  }

  public static <T> ApiResponse<T> success(int status, T data) {
    return new ApiResponse<>(status, "Success", data);
  }

  public static <T> ApiResponse<T> success(int status, String message, T data) {
    return new ApiResponse<>(status, message, data);
  }
}

