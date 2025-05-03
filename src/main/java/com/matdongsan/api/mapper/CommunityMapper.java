package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.vo.CommunityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    CommunityVO selectCommunityDetail(Long id);

    List<CommunityVO> selectCommunities(CommunityGetRequest request);
}
