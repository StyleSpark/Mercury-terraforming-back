package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.property.*;
import com.matdongsan.api.vo.PropertyMarkerVO;
import com.matdongsan.api.vo.PropertyVO;
import com.matdongsan.api.vo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

  void bulkInsert(List<Map<String, Long>> propertyTagMappings);

  List<Tag> getTags(PropertyVO vo);

  boolean checkPropertyByUserId(PropertyUpdateRequest request);

  void deletePropertyTags(Long propertyId, Set<Long> toRemove);

  List<Tag> getTagsByPropertyId(Long propertyId);

  List<PropertyVO> selectPropertiesWithinBounds(MapBoundsRequestDto request);

  int countPropertiesWithinBounds(MapBoundsRequestDto request);

  List<PropertyMarkerVO> selectPropertyMarkersWithinBounds(MapBoundsRequestDto request);
}
