package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.community.CommunityCreateRequest;
import com.matdongsan.api.dto.community.CommunityDeleteRequest;
import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.dto.community.CommunityUpdateRequest;
import com.matdongsan.api.vo.CommunityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    CommunityVO selectCommunityDetail(Long id);

    List<CommunityVO> selectCommunities(CommunityGetRequest request);

    Long insertCommunity(CommunityCreateRequest request);

    void updateCommunity(@Param("id") Long id, @Param("request") CommunityUpdateRequest request);

    void deleteCommunity(@Param("id") Long id, @Param("request") CommunityDeleteRequest request);
}
