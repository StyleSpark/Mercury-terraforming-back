package com.matdongsan.api.service;

import com.matdongsan.api.dto.agent.*;
import com.matdongsan.api.external.agent.verifier.AgentLicenseVerifier;
import com.matdongsan.api.mapper.AgentMapper;
import com.matdongsan.api.mapper.AgentReviewMapper;
import com.matdongsan.api.mapper.ReservationMapper;
import com.matdongsan.api.mapper.UserMapper;
import com.matdongsan.api.vo.AgentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AgentService {

  private final AgentMapper agentMapper;
  private final UserMapper userMapper;
  private final AgentLicenseVerifier licenseVerifier;
  private final ReservationMapper reservationMapper;
  private final AgentReviewMapper agentReviewMapper;

  /**
   * 중개인 단일 조회
   * @param agentId 중개인 고유 id
   * @return 중개인 데이터
   */
  @Transactional(readOnly = true)
  public AgentVO getAgentDetail(Long agentId) {
    return agentMapper.selectAgentDetail(agentId);
  }

  /**
   * 중개인 목록 조회
   * @param request 지역:address, 매물명:propertyName, 매물 유형:propertyType, 중개인 이름:agentName, 브랜드명:brandName
   * @return AgentGetResponse, 검색 결과 총 갯수, 페이지, 페이지 사이즈
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getAgentListWithPagination(AgentGetRequest request) {
    List<AgentGetResponse> agents = agentMapper.selectAgents(request);
    Integer total = agentMapper.countAgents(request);

    return Map.of(
            "agents", agents,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 중개인 등록 (회원 -> 중개인 전환)
   * @param request AgentRegisterRequest
   * @return 중개인 등록 성공 여부
   */
  public void registerAgent(AgentRegisterRequest request) {
    // 중개인인지 확인 (외부 API를 호출하여 자동으로 승인하는 방향으로 설계)
    if(!licenseVerifier.verify(request)) {
      throw new IllegalArgumentException("공인중개사 정보가 확인되지 않았습니다.");
    }

    // Agent 등록
    agentMapper.insertAgent(request);

    // 사용자 상태 변경
    userMapper.updateAgentStatus(request.getUserId());
  }

  /**
   * 중개인 삭제 (더 이상 '중개인'으로써 활동을 하고 싶지 않을 때라 가정, agents 테이블의 deleted_at 값 변경 / soft-delete)
   * @param request userId TODO: 로그인 사용자 인증 구현 후 수정
   */
  public void deleteAgent(AgentDeleteRequest request) {
    Long userId = request.getUserId();
    log.info(userId.toString());
    agentMapper.softDeleteAgentByUserId(userId);
  }

  /**
   * 중개인 수정 (이직, 사무소 이동, 자기소개 등)
   * @param request AgentUpdateRequest, userId
   */
  public void updateAgent(AgentUpdateRequest request, Long userId) {
    agentMapper.updateAgent(request, userId);
  }

  /**
   * 중개인 리뷰 작성
   * @param agentId '중개인' 고유 id
   * @param userId  로그인한 '회원' 고유 id
   * @param request AgentReviewCreateRequest (매물 id, 리뷰 내용, 평점)
   */
  public void createReview(Long agentId, Long userId, AgentReviewCreateRequest request) {
    // '회원'만이 '중개인'에게 리뷰를 작성할 수 있어야 함.
    boolean isNotAgent = userMapper.checkNotAgent(userId);

    // '회원'이 '중개인'과 거래 기록이 있는 경우를 확인 (현재로써는 예약이 이행되었는지로 확인)
    boolean hasReservation = reservationMapper.existsCompletedReservation(userId, agentId, request.getPropertyId());

    // '회원'은 '중개인'에게 하나의 리뷰만 작성이 가능해야 함.
    boolean existsReview = agentReviewMapper.existsByUserByAgent(userId, agentId);

    // 리뷰 작성
    agentReviewMapper.insertAgentReview(userId, agentId, request);
  }

  /**
   * 특정 중개인의 리뷰 목록 조회
   * @param agentId 중개인 고유 id
   * @param request agentId, 페이지, 사이즈
   * @return 사용자 이름, 리뷰 내용, 평점, 생성일자, 리뷰 총 갯수, 페이지, 사이즈
   */
  public Map<String, Object> getAgentReviewListWithPagination(AgentReviewGetRequest request) {
    List<AgentReviewGetResponse> agentReviews = agentReviewMapper.selectAgentReviews(request);
    Integer total = agentReviewMapper.countAgentReviews(request);

    return Map.of(
            "agentReviews", agentReviews,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

}
