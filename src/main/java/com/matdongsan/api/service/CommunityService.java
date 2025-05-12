package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.CommunityCreateRequest;
import com.matdongsan.api.dto.community.CommunityDeleteRequest;
import com.matdongsan.api.dto.community.CommunityGetRequest;
import com.matdongsan.api.dto.community.CommunityUpdateRequest;
import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.mapper.ReactionMapper;
import com.matdongsan.api.vo.CommunityVO;
import com.matdongsan.api.vo.ReactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

  private final CommunityMapper communityMapper;
  private final ReactionMapper reactionMapper;

  /**
   * 커뮤니티 상세 조회
   *
   * @param communityId 커뮤니티 communityId
   * @return 커뮤니티 상세 데이터
   */
  public CommunityVO getCommunityDetail(Long communityId) {
    communityMapper.updateCommunityViewCount(communityId); // TODO: 유저당 중복 방지를 구현해야할 지?
    return communityMapper.selectCommunityDetail(communityId);
  }

  /**
   * 커뮤니티 목록 조회
   *
   * @param request 커뮤니티 검색 조건
   * @return 커뮤니티 목록
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getCommunityListWithPagination(CommunityGetRequest request) {
    List<CommunityVO> communities = communityMapper.selectCommunities(request);
    Integer total = communityMapper.countCommunities(request);
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
    communityMapper.insertCommunity(request);
    return request.getId();
  }

  /**
   * 커뮤니티 글 수정
   *
   * @param request 커뮤니티 id, 수정 데이터
   */
  public void updateCommunity(CommunityUpdateRequest request) {
    communityMapper.updateCommunity(request);
  }

  /**
   * 커뮤니티 글 삭제
   *
   * @param request 커뮤니티 id, 사용자 정보 데이터
   */
  public void deleteCommunity(CommunityDeleteRequest request) {
    communityMapper.softDeleteCommunity(request);
  }

  /**
   * 커뮤니티 게시글 반응(좋아요/싫어요) 등록
   * @param request 유저 id, target_id(커뮤니티 id), target_type(커뮤니티), reactionType
   * @return reations id
   */
  public Long createCommunityReaction(ReactionRequest request) {
    String reactionType = request.getReactionType().toUpperCase();

    ReactionVO existing = reactionMapper.selectReaction(request);

    if (existing == null) return reactionMapper.insertReaction(request);

    if (reactionType.equals(existing.getReactionType())) {
      request.setReactionType("DEFAULT");
      reactionMapper.updateReaction(request);
      return existing.getId();
    } else {
      reactionMapper.updateReaction(request);
      return existing.getId();
    }

  }


}
