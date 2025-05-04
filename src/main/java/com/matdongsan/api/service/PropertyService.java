package com.matdongsan.api.service;

import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyGetRequest;
import com.matdongsan.api.mapper.PropertyDetailMapper;
import com.matdongsan.api.mapper.PropertyImagesMapper;
import com.matdongsan.api.mapper.PropertyMapper;
import com.matdongsan.api.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PropertyService {

  private final PropertyMapper mapper;

  private final PropertyDetailMapper detailMapper;

  private final PropertyImagesMapper imagesMapper;

  public Long createProperty(PropertyCreateRequest request) {
    //property 생성
    Long propertyId = mapper.insertProperty(request);
    if (propertyId == null) throw new RuntimeException("매물 저장 실패");
    
    //property_detail 생성
    // s3를 통해 이미지 url 추출 로직 추가 해야함
    request.getDetail().setPropertyId(propertyId);
    int detailResult = detailMapper.insertPropertyDetail(request.getDetail());
    if (detailResult != 1) throw new RuntimeException("상세 저장 실패");

    //property_images 생성
    if (request.getImages() != null) {
      for (String imageUrl : request.getImageUrls()) {
        int imageResult = imagesMapper.insertPropertyImage(propertyId, imageUrl);
        if (imageResult != 1) throw new RuntimeException("이미지 저장 실패: " + imageUrl);
      }
    }
    return propertyId;
  }

  @Transactional(readOnly = true)
  public List<PropertyVO> getProperties(PropertyGetRequest request) {
    return mapper.selectProperties(request);
  }
}
