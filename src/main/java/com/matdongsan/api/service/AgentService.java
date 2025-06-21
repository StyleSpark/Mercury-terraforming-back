package com.matdongsan.api.service;

import com.matdongsan.api.dto.agent.*;
import com.matdongsan.api.external.agent.verifier.AgentLicenseVerifier;
import com.matdongsan.api.mapper.*;
import com.matdongsan.api.vo.AgentVO;
import com.matdongsan.api.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AgentService {

  private final AgentMapper agentMapper;
  private final UserMapper userMapper;
  private final PropertyMapper propertyMapper;
  private final AgentLicenseVerifier licenseVerifier;
  private final ReservationMapper reservationMapper;
  private final AgentReviewMapper agentReviewMapper;
  private final S3Service s3Service;

  /**
   * 중개인 상세 정보
   * @param agentId
   * @return
   */
  @Transactional(readOnly = true)
  public AgentVO getAgentDetail(Long agentId) {
    return agentMapper.selectAgentDetail(agentId);
  }

  /**
   * 중개인 조회
   * @param request
   * @return
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getAgentListWithPagination(AgentGetRequest request) {
    return Map.of(
            "agents", agentMapper.selectAgents(request),
            "total", agentMapper.countAgents(request),
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 중개인 등록
   * @param request
   * @return
   * @throws Exception
   */
  public int registerAgent(AgentRegisterRequest request) throws Exception {
    // 1. 공인중개사 검증
    if (!licenseVerifier.verify(request)) {
      throw new IllegalArgumentException("공인중개사 정보가 확인되지 않았습니다.");
    }

    // 2. 중복 등록 체크
    if (agentMapper.existAgent(request)) {
      throw new IllegalArgumentException("이미 등록된 중개인입니다.");
    }

    // 3. S3 업로드
    String uploadedKey = null;
    if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
      uploadedKey = String.format("agents/%s/%s",
              request.getJurirno(), request.getProfileImage().getOriginalFilename());

      try {
        String url = s3Service.uploadFile(uploadedKey, request.getProfileImage());
        request.setProfileUrl(url);
      } catch (IOException e) {
        throw new RuntimeException("S3 이미지 업로드 실패", e);
      }

      // 4. 트랜잭션 실패 시 해당 이미지 삭제 예약
      final String keyToDelete = uploadedKey; // effectively final
      TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
        @Override
        public void afterCompletion(int status) {
          if (status != STATUS_COMMITTED) {
            s3Service.delete(keyToDelete);
          }
        }
      });
    }

    // 5. DB 등록
    int result = agentMapper.insertAgent(request);
    result += userMapper.updateAgentStatus(request.getUserId());

    return result;
  }

  /**
   * 중개인 삭제
   * @param request
   */
  public void deleteAgent(AgentDeleteRequest request) {
    agentMapper.softDeleteAgentByUserId(request.getUserId());
  }

  /**
   * 중개인 수정
   * @param request
   * @param userId
   */
  public void updateAgent(AgentUpdateRequest request, Long userId) {
    agentMapper.updateAgent(request, userId);
  }

  /**
   * 중개인 리뷰 생성
   * @param request
   */
  public void createReview(AgentReviewCreateRequest request) {
    if (!userMapper.checkNotAgent(request.getUserId())) return;
    if (!reservationMapper.existsCompletedReservation(request)) return;
    if (agentReviewMapper.existsByUserByAgent(request.getUserId(), request.getAgentId())) return;

    agentReviewMapper.insertAgentReview(request);
  }

  /**
   * 중개인 리뷰 조회
   * @param request
   * @return
   */
  public Map<String, Object> getAgentReviewListWithPagination(AgentReviewGetRequest request) {
    return Map.of(
            "agentReviews", agentReviewMapper.selectAgentReviews(request),
            "total", agentReviewMapper.countAgentReviews(request),
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 중개인 리뷰 수정
   * @param request
   */
  public void updateAgentReview(AgentReviewUpdateRequest request) {
    if (!agentReviewMapper.existsByReviewByUser(request)) return;
    agentReviewMapper.updateReview(request);
  }

  /**
   * 중개인 리뷰 삭제
   * @param reviewId
   * @param userId
   */
  public void deleteAgentReview(Long reviewId, Long userId) {
    agentReviewMapper.softDeleteAgentReview(reviewId, userId);
  }

  /**
   * 지도범위 중개인 조회
   * @param request
   * @return
   */
  public List<AgentGetResponse> getAgentsWithinBounds(AgentBoundsRequest request) {
    return agentMapper.selectPropertiesWithinBounds(request);
  }

  /**
   * 중개인 매물 조회
   * @param agentId
   * @param page
   * @param size
   * @return
   */
  public Map<String, Object> getPropertiesByAgent(Long agentId, int page, int size) {
    Long userId = agentMapper.selectUserIdByAgentId(agentId);
    return Map.of(
            "properties", propertyMapper.selectUserProperties(userId),
            "total", agentMapper.countPropertiesByAgent(userId),
            "page", page,
            "size", size
    );
  }
}
