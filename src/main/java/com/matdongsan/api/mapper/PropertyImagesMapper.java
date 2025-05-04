package com.matdongsan.api.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyImagesMapper {
  int insertPropertyImage(Long propertyId, String imageUrl);
}
