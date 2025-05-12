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
  CommunityVO selectCommunityDetail(Long id);

  List<CommunityVO> selectCommunities(CommunityGetRequest request);

  Long insertCommunity(CommunityCreateRequest request);

  void updateCommunity(CommunityUpdateRequest request);

  void softDeleteCommunity(CommunityDeleteRequest request);

  Integer countCommunities(CommunityGetRequest request);

  void updateCommunityViewCount(Long communityId);
}
