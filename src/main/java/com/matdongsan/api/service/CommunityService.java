package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.*;
import com.matdongsan.api.dto.reaction.ReactionCreateRequest;
import com.matdongsan.api.mapper.CommunityCommentMapper;
import com.matdongsan.api.mapper.CommunityImagesMapper;
import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.mapper.ReactionMapper;
import com.matdongsan.api.vo.CommunityVO;
import com.matdongsan.api.vo.ReactionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

  @Transactional(readOnly = true)
  public CommunityVO getCommunityDetail(Long communityId, Long loginUserId) {
    CommunityVO community = communityMapper.selectCommunityDetail(communityId);

    if (community == null) {
      throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
    }

    boolean isMine = loginUserId != null && loginUserId.equals(community.getUserId());
    String myReaction = "DEFAULT";
    if (loginUserId != null) {
      String result = reactionMapper.isMyReation(loginUserId, communityId, targetType);
      if (result != null) {
        myReaction = result;
      }
    }
    Long likeCount = reactionMapper.selectReactionLikeCount(communityId, targetType);
    Long dislikeCount = reactionMapper.selectReactionDislikeCount(communityId, targetType);

    community.setIsMine(isMine);
    community.setMyReaction(myReaction);
    community.setLikeCount(likeCount);
    community.setDislikeCount(dislikeCount);

    return community;
  }

  public void increaseViewCount(Long communityId) {
    if (communityMapper.updateCommunityViewCount(communityId) != 1) {
      throw new IllegalArgumentException("조회 수 업데이트에 실패하였습니다.");
    }
  }

  public void updateCommunity(
          CommunityUpdateRequest request,
          List<MultipartFile> images,
          Long loginUserId) {
    if (loginUserId == null) {
      throw new RuntimeException("로그인을 하셔야 게시물 수정을 할 수 있습니다.");
    }
    request.setUserId(loginUserId);

    Long communityId = request.getId();
    validateCommunityOwnerShip(communityId, loginUserId);

    CommunityVO community = communityMapper.selectCommunityDetail(communityId);
    if (community == null) {
      throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
    }

    List<String> registeredImageUrls = communityImagesMapper.selectImageUrlsByCommunityId(communityId);
    List<String> newImageUrls = extractImageUrls(request.getContent());

    List<String> deletedImageUrls = new ArrayList<>();
    for (String registeredImageUrl : registeredImageUrls) {
      if (!newImageUrls.contains(registeredImageUrl)) {
        deletedImageUrls.add(registeredImageUrl);
      }
    }

    List<String> uploadedUrls = new ArrayList<>();
    String updatedContent = request.getContent();
    if (images != null && !images.isEmpty()) {
      uploadedUrls = uploadImage(communityId, images);
      updatedContent = replaceBlobUrls(request.getContent(), request.getBlobUrlMap(), uploadedUrls);
    }

    String thumbnailUrl = extractFirstImageUrl(updatedContent);

    request.setContent(updatedContent);
    request.setThumbnailUrl(thumbnailUrl);

    if (communityMapper.updateCommunity(request) != 1) {
      throw new RuntimeException("게시글 수정이 실패했습니다.");
    }

    for (String imageUrl : deletedImageUrls) {
      try {
        s3Service.deleteByUrl(imageUrl);
        if (communityImagesMapper.softDeleteByUrl(communityId, imageUrl) != 1) {
          throw new RuntimeException("이미지 테이블 soft-delete 삭제 실패");
        }
      } catch (Exception e) {
        throw new RuntimeException("이미지 삭제 실패: {}", e);
      }
    }
  }

  public void deleteCommunity(CommunityDeleteRequest request) {
    if (request.getUserId() == null) {
      throw new RuntimeException("로그인을 하셔야 게시물 삭제를 할 수 있습니다.");
    }

    Long communityId = request.getId();
    Long loginUserId = request.getUserId();
    validateCommunityOwnerShip(communityId, loginUserId);

    List<String> deletedImageUrls = communityImagesMapper.selectImageUrlsByCommunityId(communityId);

    for (String imageUrl : deletedImageUrls) {
      try {
        s3Service.deleteByUrl(imageUrl);
        if (communityImagesMapper.softDeleteByUrl(communityId, imageUrl) != 1) {
          throw new RuntimeException("이미지 테이블 soft-delete 삭제 실패");
        }
      } catch (Exception e) {
        throw new RuntimeException("이미지 삭제 실패: {}", e);
      }
    }

    // 댓글/대댓글 삭제 처리

    // 리액션(좋아요/싫어요) 삭제 처리

    if (communityMapper.softDeleteCommunity(request) != 1) {
      throw new RuntimeException("게시글 삭제가 실패했습니다.");
    }
  }

  public Long createCommunityReaction(ReactionCreateRequest request) {
    request.setTargetType(targetType);
    String reactionType = request.getReactionType().toUpperCase();

    ReactionVO existing = reactionMapper.selectReaction(request); // ReactionVO 점검

    if (existing == null) {
      if (reactionMapper.insertReaction(request) != 1) {
        throw new RuntimeException("반응 등록에 실패하였습니다.");
      } else {
        return request.getId();
      }
    }

    if (reactionType.equals(existing.getReactionType())) {
      request.setReactionType("DEFAULT");
      if (reactionMapper.updateReaction(request) != 1) { // 요청 DTO에 'id'를 만들면 쿼리의 WHERE절 수정 가능
        throw new RuntimeException("반응 업데이트에 실패하였습니다.");
      }
      return existing.getId();
    } else {
      if (reactionMapper.updateReaction(request) != 1) {
        throw new RuntimeException("반응 업데이트에 실패하였습니다.");
      }
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
        for (String uploadedKey : uploadedKeys) {
          try {
            s3Service.delete(uploadedKey); // deleteByUrl을 사용하여 uploadedKeys 리스트는 필요 없지 않나
          } catch (Exception rollbackEx) {
            throw new RuntimeException("업로드 된 S3 삭제 실패", e);
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

  private void validateCommunityOwnerShip(Long communityId, Long loginUserId) {
    if (!communityMapper.checkCommunityByUserId(communityId, loginUserId)) {
      throw new SecurityException("게시글 작성자만 이용할 수 있습니다.");
    }
  }

  private List<String> extractImageUrls(String html) {
    List<String> urls = new ArrayList<>();
    if (html == null || html.isEmpty()) return urls;

    Document doc = Jsoup.parse(html);
    Elements images = doc.select("img");

    for (Element img : images) {
      String src = img.attr("src");
      if (src != null && !src.isEmpty()) {
        urls.add(src);
      }
    }

    return urls;
  }

  private String extractFirstImageUrl(String html) {
    if (html == null || html.isEmpty()) return null;

    Document doc = Jsoup.parse(html);
    Element img = doc.selectFirst("img");

    if (img != null) {
      String src = img.attr("src");
      if (src != null && !src.isEmpty()) {
        return src;
      }
    }

    return null;
  }


}
