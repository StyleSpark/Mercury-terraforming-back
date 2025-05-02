package com.matdongsan.api.mapper;

import com.matdongsan.api.vo.CommunityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommunityMapper {
    CommunityVO getCommunityById(@Param("id") Long id);
}
