package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.property.PropertyDeleteRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PropertyImagesMapper {
  int insertPropertyImage(@Param("propertyId") Long propertyId, @Param("imageUrl") String imageUrl);

  List<String> selectImageUrlsByPropertyId(Long id);

  int softDeletePropertyImages(PropertyDeleteRequest id);

  int softDeletePropertyImagesByPropertyId(Long propertyId);
}
