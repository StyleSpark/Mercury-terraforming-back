package com.matdongsan.api.service;

import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.dto.reservation.ReservationGetDto;
import com.matdongsan.api.dto.reservation.ReservationTimeGetDto;
import com.matdongsan.api.mapper.AgentMapper;
import com.matdongsan.api.mapper.ReservationMapper;
import com.matdongsan.api.vo.ReservationVO;
import com.matdongsan.api.vo.ReservedTimeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ReservationService {

  private final ReservationMapper mapper;
  private final AgentMapper agentMapper;
  private final NotificationRedisService redisService;

  /**
   * 매물 등록자(agentId)의 예약 알림 조회
   */
  @Transactional(readOnly = true)
  public List<ReservationVO> getNotifications(Long agentId) {
    return mapper.selectReservationsByAgentId(agentId);
  }

  /**
   * 사용자 예약 생성
   */
  public Long createReservation(ReservationCreateDto request) {
    mapper.insertReservation(request);

    // 예약된 매물의 등록자 ID 조회
    Long agentId = agentMapper.findAgentIdByPropertyId(request.getPropertyId());

    // 방금 생성된 예약 정보 가져오기 (또는 직접 구성)
    ReservationVO reservation = mapper.findLastReservationByProperty(request.getPropertyId());

    // Redis에 알림 push
    redisService.pushNotification(agentId, reservation);

    return request.getId();
  }


  /**
   * 사용자 본인의 예약 목록 조회
   */
  @Transactional(readOnly = true)
  public List<ReservationVO> getReservations(ReservationGetDto request) {
    return mapper.selectReservations(request);
  }

  /**
   * 특정 매물의 예약된 시간 조회
   */
  @Transactional(readOnly = true)
  public List<ReservedTimeVO> getReservationTimes(ReservationTimeGetDto request) {
    return mapper.selectReservationTimesByDate(request);
  }
}
