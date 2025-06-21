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

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
  public int registerAgent(AgentRegisterRequest request) throws Exception {
    int result = 0;
    // 중개인인지 확인
    if(!licenseVerifier.verify(request)) {
      throw new IllegalArgumentException("공인중개사 정보가 확인되지 않았습니다.");
    }

    //이미 등록된 중개인인지 확인해야함
    boolean isExist =agentMapper.existAgent(request);
    if(isExist){
      throw new IllegalArgumentException("이미 등록된 중개인입니다.");
    }
    // 부동산 이미지 업로드
    // 이미 등록된 중개인인지 확인 해야함
    
    // Agent 등록
    if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {

      try {
        // key 예시: agents/11620202100167/대표이미지.jpg
        String key = String.format("agents/%s/%s", request.getJurirno(), request.getProfileImage().getOriginalFilename());
        String url = s3Service.uploadFile(key, request.getProfileImage());
        request.setProfileUrl(url); // 업로드 성공 시 URL 저장
      } catch (IOException e) {
        throw new RuntimeException("S3 이미지 업로드 실패", e);
      }
    }
    result += agentMapper.insertAgent(request);

    // 사용자 상태 변경
    result += userMapper.updateAgentStatus(request.getUserId());
    return result;
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
   * @param request AgentReviewCreateRequest ('중개인' 고유 id, 로그인한 '회원' 고유 id, 매물 id, 리뷰 내용, 평점)
   */
  public void createReview(AgentReviewCreateRequest request) {
    // '회원'만이 '중개인'에게 리뷰를 작성할 수 있어야 함.
    boolean isNotAgent = userMapper.checkNotAgent(request.getUserId());

    // '회원'이 '중개인'과 거래 기록이 있는 경우를 확인 (현재로써는 예약이 이행되었는지로 확인)
    boolean hasReservation = reservationMapper.existsCompletedReservation(request);

    // '회원'은 '중개인'에게 하나의 리뷰만 작성이 가능해야 함.
    boolean existsReview = agentReviewMapper.existsByUserByAgent(request.getUserId(), request.getAgentId());

    // 리뷰 작성
    agentReviewMapper.insertAgentReview(request);
  }

  /**
   * 특정 중개인의 리뷰 목록 조회
   * @param request 중개인 고유 id, 페이지, 사이즈
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

  /**
   * 중개인 리뷰 수정
   * @param request 리뷰 id, 로그인 유저 id, 리뷰 내용, 점수
   */
  public void updateAgentReview(AgentReviewUpdateRequest request) {
    // 해당 리뷰가 존재하고, 그 리뷰가 로그인한 유저가 쓴 것인지 확인
    boolean existsReview = agentReviewMapper.existsByReviewByUser(request);

    // 리뷰 수정
    agentReviewMapper.updateReview(request);
  }

  /**
   * 중개인 리뷰 삭제
   * @param reviewId 중개인 리뷰 id
   * @param userId 로그인 사용자 id
   */
  public void deleteAgentReview(Long reviewId, Long userId) {
    agentReviewMapper.softDeleteAgentReview(reviewId, userId);
  }

  public List<AgentGetResponse> getAgentsWithinBounds(AgentBoundsRequest request) {
    return agentMapper.selectPropertiesWithinBounds(request);
  }


  public Map<String, Object> getPropertiesByAgent(Long agentId, int page, int size) {
    Long userId = agentMapper.selectUserIdByAgentId(agentId);
    List<PropertyVO> properties = propertyMapper.selectUserProperties(userId);
    int total = agentMapper.countPropertiesByAgent(userId);

    return Map.of(
            "properties", properties,
            "total", total,
            "page", page,
            "size", size
    );
  }
}
