package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.*;
import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.mapper.CommunityCommentMapper;
import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.mapper.ReactionMapper;
import com.matdongsan.api.vo.CommunityVO;
import com.matdongsan.api.vo.ReactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

  private final CommunityMapper communityMapper;
  private final ReactionMapper reactionMapper;
  private final CommunityCommentMapper communityCommentMapper;

  /**
   * 커뮤니티 상세 조회
   *
   * @param communityId 커뮤니티 communityId
   * @return 커뮤니티 상세 데이터
   */
  public CommunityGetResponse getCommunityDetail(Long communityId) {
    communityMapper.updateCommunityViewCount(communityId); // TODO: 유저당 중복 방지를 구현해야할 지?

    CommunityVO data = communityMapper.selectCommunityDetail(communityId);

    String targetType = "COMMUNITY";
    Long likeCount = reactionMapper.selectReactionLikeCount(communityId, targetType);
    Long dislikeCount = reactionMapper.selectReactionDislikeCount(communityId, targetType);

    return CommunityGetResponse.builder()
            .communityId(data.getId())
            .userId(data.getUserId())
            .title(data.getTitle())
            .content(data.getContent())
            .imageUrls(data.getImageUrls())
            .viewCount(data.getViewCount())
            .createdAt(data.getCreatedAt())
            .likeCount(likeCount)
            .dislikeCount(dislikeCount)
            .build();
  }

  /**
   * 커뮤니티 목록 조회
   *
   * @param request 커뮤니티 검색 조건
   * @return 커뮤니티 목록
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getCommunityListWithPagination(CommunityGetRequest request, Long loginUserId) {
    List<CommunityVO> communities = communityMapper.selectCommunities(request);

    // 게시글 ID 추출
    List<Long> ids = new ArrayList<>();
    for (CommunityVO vo : communities) {
      ids.add(vo.getId());
      vo.setIsMine(vo.getUserId() != null && vo.getUserId().equals(loginUserId));
    }

    // 댓글 수 조회 및 Map 변환
    List<Map<String, Object>> commentCounts = communityCommentMapper.selectCommentCountGroupByCommunity(ids);
    Map<Long, Integer> commentMap = new HashMap<>();
    for (Map<String, Object> map : commentCounts) {
      Long communityId = ((Number) map.get("community_id")).longValue();
      Integer count = ((Number) map.get("comment_count")).intValue();
      commentMap.put(communityId, count);
    }

    // 좋아요/싫어요 조회 및 Map 변환
    List<Map<String, Object>> reactions = reactionMapper.selectReactionCountGroupByTarget(ids, "COMMUNITY");
    Map<Long, Integer> likeMap = new HashMap<>();
    Map<Long, Integer> dislikeMap = new HashMap<>();
    for (Map<String, Object> r : reactions) {
      Long id = ((Number) r.get("target_id")).longValue();
      Integer likeCount = r.get("like_count") != null ? ((Number) r.get("like_count")).intValue() : 0;
      Integer dislikeCount = r.get("dislike_count") != null ? ((Number) r.get("dislike_count")).intValue() : 0;
      likeMap.put(id, likeCount);
      dislikeMap.put(id, dislikeCount);
    }

    // CommunityVO에 카운트 정보 매핑
    for (CommunityVO vo : communities) {
      Long id = vo.getId();
      vo.setCommentCount(commentMap.containsKey(id) ? commentMap.get(id) : 0);
      vo.setLikeCount(likeMap.containsKey(id) ? likeMap.get(id) : 0);
      vo.setDislikeCount(dislikeMap.containsKey(id) ? dislikeMap.get(id) : 0);
    }

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
