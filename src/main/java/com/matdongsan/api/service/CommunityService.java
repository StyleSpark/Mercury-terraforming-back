package com.matdongsan.api.service;

import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.vo.CommunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
