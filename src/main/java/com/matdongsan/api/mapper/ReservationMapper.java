package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.dto.reservation.ReservationGetDto;
import com.matdongsan.api.dto.reservation.ReservationTimeGetDto;
import com.matdongsan.api.vo.ReservationVO;
import com.matdongsan.api.vo.ReservedTimeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
  Long insertReservation(ReservationCreateDto request);

  List<ReservationVO> selectReservations(ReservationGetDto request);

  List<ReservedTimeVO> selectReservationTimesByDate(ReservationTimeGetDto request);

  boolean existsCompletedReservation(@Param("userId") Long userId, @Param("agentId") Long agentId, @Param("propertyId") Long propertyId);
}
