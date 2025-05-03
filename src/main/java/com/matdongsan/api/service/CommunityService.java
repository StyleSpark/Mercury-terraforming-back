package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.vo.CommunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

  private final CommunityMapper mapper;

  /**
   * 커뮤니티 상세 조회
   * @param id 커뮤니티 id
   * @return 커뮤니티 상세 데이터
   */

  public CommunityVO getCommunityDetail(Long id) {
    return mapper.selectCommunityDetail(id);
  }

  /**
   * 커뮤니티 목록 조회
   * @param request 커뮤니티 검색 조건
   * @return 커뮤니티 목록
   */
  public List<CommunityVO> getCommunityList(CommunityGetRequest request) {
    return mapper.selectCommunities(request);
  }
}
