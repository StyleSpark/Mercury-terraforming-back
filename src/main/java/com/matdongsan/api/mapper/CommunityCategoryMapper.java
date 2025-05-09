package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.community.CommunityDeleteRequest;
import com.matdongsan.api.dto.community.category.CategoryCreateRequest;
import com.matdongsan.api.dto.community.category.CategoryDeleteRequest;
import com.matdongsan.api.dto.community.category.CategoryGetRequest;
import com.matdongsan.api.dto.community.category.CategoryUpdateRequest;
import com.matdongsan.api.vo.CommunityCategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityCategoryMapper {
  List<CommunityCategoryVO> selectCommunityCategories(CategoryGetRequest request);

  Long insertCommunityCategory(CategoryCreateRequest request);

  void updateCommunityCategory(CategoryUpdateRequest request);

  void softDeleteCommunityCategory(CategoryDeleteRequest request);
}
