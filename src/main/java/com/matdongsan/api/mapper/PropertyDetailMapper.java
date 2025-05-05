package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyDeleteRequest;
import com.matdongsan.api.dto.property.PropertyDetailRequest;
import com.matdongsan.api.vo.PropertyDetailVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyDetailMapper {
  int insertPropertyDetail(PropertyDetailRequest detail);

  PropertyDetailVO selectPropertyDetailByPropertyId(Long id);

  int softDeletePropertyDetail(PropertyDeleteRequest request);

  int updatePropertyDetail(PropertyDetailRequest detail);
}
