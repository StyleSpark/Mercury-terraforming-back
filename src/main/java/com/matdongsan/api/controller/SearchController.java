package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.service.OpenSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "검색 API", description = "키워드/복합 검색 API")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

  private final OpenSearchService openSearchService;

  @Operation(summary = "매물 키워드 검색", description = "OpenSearch를 이용한 매물 키워드 검색")
  @GetMapping("/properties")
  public ResponseEntity<?> searchProperties(
          @RequestParam String keyword
  ) {
    try {
      List<Map<String, Object>> result = openSearchService.searchProperties(keyword);
      return ResponseEntity.ok(ApiResponse.success(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.success("검색 실패: " + e.getMessage()));
    }
  }

  @Operation(summary = "중개인 키워드 검색", description = "OpenSearch를 이용한 중개인 키워드 검색")
  @GetMapping("/agents")
  public ResponseEntity<?> searchAgents(
          @RequestParam String keyword
  ) {
    try {
      List<Map<String, Object>> result = openSearchService.searchAgents(keyword);
      return ResponseEntity.ok(ApiResponse.success(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.success("검색 실패: " + e.getMessage()));
    }
  }
}
