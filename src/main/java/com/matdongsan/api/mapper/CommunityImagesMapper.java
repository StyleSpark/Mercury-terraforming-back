package com.matdongsan.api.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommunityImagesMapper {

  Long insertCommunityImage(Long communityId, String imageUrl); // @Param 빼고 실험해보기

}
