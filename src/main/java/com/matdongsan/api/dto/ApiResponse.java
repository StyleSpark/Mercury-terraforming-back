package com.matdongsan.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "공통 API 응답 포맷")
public class ApiResponse<T> {

  @Schema(description = "HTTP 상태 코드", example = "200")
  private int status;

  @Schema(description = "응답 메시지", example = "Success")
  private String message;

  @Schema(description = "응답 데이터")
  private T data;

  private ApiResponse(int status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(200, "Success", data);
  }

  public static <T> ApiResponse<T> success(int status, T data) {
    return new ApiResponse<>(status, "Success", data);
  }

  public static <T> ApiResponse<T> success(int status, String message, T data) {
    return new ApiResponse<>(status, message, data);
  }
}
