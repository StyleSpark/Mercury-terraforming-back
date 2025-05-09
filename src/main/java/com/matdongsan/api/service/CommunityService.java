package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.CommunityCreateRequest;
import com.matdongsan.api.dto.community.CommunityDeleteRequest;
import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.dto.community.CommunityUpdateRequest;
import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.vo.CommunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

  private final CommunityMapper mapper;

  /**
   * 커뮤니티 상세 조회
   *
   * @param id 커뮤니티 id
   * @return 커뮤니티 상세 데이터
   */
  @Transactional(readOnly = true)
  public CommunityVO getCommunityDetail(Long id) {
    return mapper.selectCommunityDetail(id);
  }

  /**
   * 커뮤니티 목록 조회
   *
   * @param request 커뮤니티 검색 조건
   * @return 커뮤니티 목록
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getCommunityListWithPagination(CommunityGetRequest request) {
    List<CommunityVO> communities = mapper.selectCommunities(request);
    Integer total = mapper.countCommunities(request);
    return Map.of(
            "communities", communities,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 커뮤니티 글 생성
   *
   * @param request 커뮤니티 등록 데이터
   * @return 생성된 커뮤니티 id
   */
  public Long createCommunity(CommunityCreateRequest request) {
    mapper.insertCommunity(request);
    return request.getId();
  }

  /**
   * 커뮤니티 글 수정
   *
   * @param request 커뮤니티 id, 수정 데이터
   */
  public void updateCommunity(CommunityUpdateRequest request) {
    mapper.updateCommunity(request);
  }

  /**
   * 커뮤니티 글 삭제
   *
   * @param request 커뮤니티 id, 사용자 정보 데이터
   */
  public void deleteCommunity(CommunityDeleteRequest request) {
    mapper.softDeleteCommunity(request);
  }
}
