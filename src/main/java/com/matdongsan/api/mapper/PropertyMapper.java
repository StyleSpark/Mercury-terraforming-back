package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.property.PropertyCreateRequest;
import com.matdongsan.api.dto.property.PropertyDeleteRequest;
import com.matdongsan.api.dto.property.PropertyGetRequest;
import com.matdongsan.api.dto.property.PropertyUpdateRequest;
import com.matdongsan.api.vo.PropertyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PropertyMapper {

  Long insertProperty(PropertyCreateRequest request);

  List<PropertyVO> selectProperties(PropertyGetRequest request);

  PropertyVO selectPropertyById(Long id);

  int softDeleteProperty(PropertyDeleteRequest request);

  int updateProperty(PropertyUpdateRequest request);
}
