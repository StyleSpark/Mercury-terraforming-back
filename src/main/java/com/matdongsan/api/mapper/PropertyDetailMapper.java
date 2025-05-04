package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyDetailRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyDetailMapper {
  int insertPropertyDetail(PropertyDetailRequest detail);
}
