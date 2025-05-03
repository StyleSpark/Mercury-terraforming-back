package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.CommunityCreateRequest;
import com.matdongsan.api.dto.community.CommunityDeleteRequest;
import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.dto.community.CommunityUpdateRequest;
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

  /**
   * 커뮤니티 글 생성
   * @param request 커뮤니티 등록 데이터
   * @return 생성된 커뮤니티 id
   */
  public Long createCommunity(CommunityCreateRequest request) {
    return mapper.insertCommunity(request);
  }

  /**
   * 커뮤니티 글 수정
   * @param id 해당 커뮤니티 id
   * @param request 커뮤니티 수정 데이터
   */
  public void updateCommunity(Long id, CommunityUpdateRequest request) {
    mapper.updateCommunity(id, request);
  }

  /**
   * 커뮤니티 글 삭제
   * @param id 해당 커뮤니티 id
   * @param request 사용자 정보 데이터
   */
  public void deleteCommunity(Long id, CommunityDeleteRequest request) {
    mapper.deleteCommunity(id, request);
  }
}
