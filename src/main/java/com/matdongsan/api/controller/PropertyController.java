package com.matdongsan.api.controller;

import com.matdongsan.api.dto.ApiResponse;
import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyGetRequest;
import com.matdongsan.api.service.PropertyService;
import com.matdongsan.api.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/properties")
public class PropertyController {

  final private PropertyService service;

  /**
   * 매물 등록
   * @param request
   * @param images
   * @param thumbnail
   * @return
   */
  @PostMapping
  public ResponseEntity<?> createProperty(
          @RequestPart("request") PropertyCreateRequest request,
          @RequestPart(value = "images", required = false) List<MultipartFile> images,
          @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {
    request.setImages(images);
    request.setThumbnail(thumbnail);
    Long id = service.createProperty(request);
    return ResponseEntity.ok(ApiResponse.success(id));
  }
  //매물 리스트 조회
  @GetMapping
  public ResponseEntity<?> getProperties(PropertyGetRequest request){
    List<PropertyVO> properties = service.getProperties(request);
    return ResponseEntity.ok(ApiResponse.success(properties));
  }

  //매물 상세 조회

  // 매물 댓글 작성

  // 매물 댓글 조회

  // 매물 댓글 수정

  // 매물 댓글 삭제
}
