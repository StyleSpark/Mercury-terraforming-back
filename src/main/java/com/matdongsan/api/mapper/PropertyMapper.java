package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.property.*;
import com.matdongsan.api.vo.PropertyVO;
import com.matdongsan.api.vo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PropertyMapper {

  Long insertProperty(PropertyCreateRequest request);

  List<PropertyVO> selectProperties(PropertyGetRequest request);

  PropertyVO selectPropertyById(Long id);

  int softDeleteProperty(PropertyDeleteRequest request);

  int updateProperty(PropertyUpdateRequest request);

  List<Long> getFavoritePropertyIds(Long userId, List<Long> propertyIds);

  List<PropertyVO> selectUserProperties(Long id);

  void insertPropertyTags(PropertyCreateRequest request);

  List<Tag> findTagsByNames(List<String> tagNames);

  void insertIgnoreDuplicates(List<String> newTags);

  void bulkInsert(List<Map<String, Object>> propertyTagMappings);

  List<Tag> getTags(PropertyVO vo);
}
