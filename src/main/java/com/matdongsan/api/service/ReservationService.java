package com.matdongsan.api.service;

import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.dto.reservation.ReservationGetDto;
import com.matdongsan.api.dto.reservation.ReservationTimeGetDto;
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

  public Long createReservation(ReservationCreateDto request) {
    return mapper.insertReservation(request);
  }

  @Transactional(readOnly = true)
  public List<ReservationVO> getReservations(ReservationGetDto request) {
    return mapper.selectReservations(request);
  }

  public List<ReservedTimeVO> getReservationTimes(ReservationTimeGetDto request) {
    return mapper.selectReservationTimesByDate(request);
  }
}
