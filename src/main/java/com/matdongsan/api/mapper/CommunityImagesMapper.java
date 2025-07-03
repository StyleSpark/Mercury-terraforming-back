package com.matdongsan.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityImagesMapper {
  Long insertCommunityImage(Long communityId, String imageUrl);

  List<String> selectImageUrlsByCommunityId(Long communityId);

  int softDeleteByUrl(Long communityId, String imageUrl);
}
