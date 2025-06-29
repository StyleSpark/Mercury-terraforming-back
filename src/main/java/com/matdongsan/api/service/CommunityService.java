package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.*;
import com.matdongsan.api.dto.reaction.ReactionRequest;
import com.matdongsan.api.mapper.CommunityCommentMapper;
import com.matdongsan.api.mapper.CommunityImagesMapper;
import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.mapper.ReactionMapper;
import com.matdongsan.api.vo.CommunityVO;
import com.matdongsan.api.vo.ReactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

  private final CommunityMapper communityMapper;
  private final ReactionMapper reactionMapper;
  private final CommunityCommentMapper communityCommentMapper;
  private final CommunityImagesMapper communityImagesMapper;

  private final ImageConversionService imageConversionService;
  private final S3Service s3Service;

  private final String targetType = "COMMUNITY";

  public Long createCommunity(
          CommunityCreateRequest request,
          List<MultipartFile> images,
          Long loginUserId) {

    if (loginUserId == null) {
      throw new RuntimeException("로그인을 하셔야 게시물 등록을 할 수 있습니다.");
    }
    request.setUserId(loginUserId);

    Long communityId = null;

    try {
      communityMapper.insertCommunity(request);
      communityId = Optional.ofNullable(request.getId())
              .orElseThrow(() -> new RuntimeException("커뮤니티 저장 실패"));

      // 이미지가 있을 경우
      if (images != null && !images.isEmpty()) {
        List<String> urls = uploadImage(communityId, images);
        String thumbnailUrl = urls.get(0);
        String content = replaceBlobUrls(request.getContent(), request.getBlobUrlMap(), urls);
        if (!communityMapper.updateCommunityContentAndThumbnailUrl(communityId, content, thumbnailUrl)) {
          throw new RuntimeException("Content 업데이트 실패");
        }
      }
    } catch (Exception e) {
      // 게시글 등록이 실패한다면 DB 롤백을 위한 실제 삭제(hard delete) 처리
      if (communityId != null) {
          if (!communityMapper.rollbackCommunityInsert(communityId)) {
            throw new RuntimeException("게시물 등록 롤백 처리 중 오류 발생", e);
          }
      }
    }
    return communityId;
  }

  @Transactional(readOnly = true)
  public Map<String, Object> getCommunityListWithPagination(CommunityGetRequest request, Long loginUserId) {
    List<CommunityVO> communities = communityMapper.selectCommunities(request);

    if (communities.isEmpty()) {
      return Map.of(
              "communities", List.of(),
              "total", 0,
              "page", request.getPage(),
              "size", request.getSize()
      );
    }

    List<Long> Communityids = new ArrayList<>();
    for (CommunityVO vo : communities) {
      Communityids.add(vo.getId());
    }

    List<Map<String, Object>> commentCounts = communityCommentMapper.selectCommentCountGroupByCommunity(Communityids);
    Map<Long, Long> commentMap = new HashMap<>();
    for (Map<String, Object> map : commentCounts) {
      Long communityId = ((Number) map.get("community_id")).longValue();
      Long count = ((Number) map.get("comment_count")).longValue();
      commentMap.put(communityId, count);
    }

    List<Map<String, Object>> reactions = reactionMapper.selectReactionCountGroupByTarget(Communityids, targetType);
    Map<Long, Long> likeMap = new HashMap<>();
    Map<Long, Long> dislikeMap = new HashMap<>();
    for (Map<String, Object> r : reactions) {
      Long id = ((Number) r.get("target_id")).longValue();
      Long likeCount = r.get("like_count") != null ? ((Number) r.get("like_count")).longValue() : 0;
      Long dislikeCount = r.get("dislike_count") != null ? ((Number) r.get("dislike_count")).longValue() : 0;
      likeMap.put(id, likeCount);
      dislikeMap.put(id, dislikeCount);
    }

    for (CommunityVO vo : communities) {
      Long communityId = vo.getId();
      vo.setIsMine(vo.getUserId() != null && vo.getUserId().equals(loginUserId));
      vo.setCommentCount(commentMap.containsKey(communityId) ? commentMap.get(communityId) : 0);
      vo.setLikeCount(likeMap.containsKey(communityId) ? likeMap.get(communityId) : 0);
      vo.setDislikeCount(dislikeMap.containsKey(communityId) ? dislikeMap.get(communityId) : 0);
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
   * 커뮤니티 상세 조회
   *
   * @param communityId 커뮤니티 communityId
   * @return 커뮤니티 상세 데이터
   */
  public CommunityGetResponse getCommunityDetail(Long communityId, Long loginUserId) {
    communityMapper.updateCommunityViewCount(communityId);

    CommunityVO data = communityMapper.selectCommunityDetail(communityId);
    boolean isMine = loginUserId != null && loginUserId.equals(data.getUserId());

    String targetType = "COMMUNITY";
    Long likeCount = reactionMapper.selectReactionLikeCount(communityId, targetType);
    Long dislikeCount = reactionMapper.selectReactionDislikeCount(communityId, targetType);

    return CommunityGetResponse.builder()
            .communityId(data.getId())
            .userId(data.getUserId())
            .userName(data.getUserName())
            .title(data.getTitle())
            .content(data.getContent())
            .imageUrls(data.getImageUrls())
            .viewCount(data.getViewCount())
            .createdAt(data.getCreatedAt())
            .likeCount(likeCount)
            .dislikeCount(dislikeCount)
            .isMine(isMine)
            .build();
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

  public List<String> uploadImage(Long communityId, List<MultipartFile> images) {
    List<String> uploadedUrls = new ArrayList<>();
    List<String> uploadedKeys = new ArrayList<>();

    for (MultipartFile image : images) {
      try {
        byte[] data = imageConversionService.convertToWebP(image);
        String key = "communities/" + communityId + "_" + System.currentTimeMillis() + ".webp";
        String url = s3Service.uploadBytes(key, data, "image/webp");
        uploadedUrls.add(url);
        uploadedKeys.add(key);
        if (communityImagesMapper.insertCommunityImage(communityId, url) != 1) {
          throw new RuntimeException("이미지 URL 저장 실패: " + url);
        }
      } catch (Exception e) {
        // 앞서 업로드된 이미지 롤백 처리
        for (String uploadedKey : uploadedKeys) {
          try {
            s3Service.delete(uploadedKey);
          } catch (Exception rollbackEx) {
            throw new RuntimeException("업로드 된 s3 삭제 실패", e);
          }
        }
        throw new RuntimeException("이미지 업로드 실패", e);
      }
    }
    return uploadedUrls;
  }

  private String replaceBlobUrls(String content, Map<String, Integer> blobUrlMap, List<String> uploadedUrls) {
    if (blobUrlMap == null || uploadedUrls == null) return content;

    for (Map.Entry<String, Integer> entry : blobUrlMap.entrySet()) {
      String blobUrl = entry.getKey();
      int index = entry.getValue();

      if (index >= 0 && index < uploadedUrls.size()) {
        String s3Url = uploadedUrls.get(index);
        content = content.replace(blobUrl, s3Url);
      }
    }
    return content;
  }

}
