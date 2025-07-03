package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.community.CommunityCreateRequest;
import com.matdongsan.api.dto.community.CommunityDeleteRequest;
import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.dto.community.CommunityUpdateRequest;
import com.matdongsan.api.vo.CommunityVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {
  int insertCommunity(CommunityCreateRequest request);

  boolean updateCommunityContentAndThumbnailUrl(Long communityId, String content, String thumbnailUrl);

  boolean rollbackCommunityInsert(Long communityId);

  List<CommunityVO> selectCommunities(CommunityGetRequest request);

  Integer countCommunities(CommunityGetRequest request);

  CommunityVO selectCommunityDetail(Long id);

  int updateCommunityViewCount(Long communityId);

  boolean checkCommunityByUserId(CommunityUpdateRequest request);

  int updateCommunity(CommunityUpdateRequest request);

  void softDeleteCommunity(CommunityDeleteRequest request);
}
