package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.payment.PaymentCreateDto;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.vo.TempReservationVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
  int insertTempReservation(ReservationCreateDto request);

  TempReservationVO findTempByOrderId(String orderId);

  int insertConfirmedReservation(ReservationCreateDto reservation);

  int deleteTempByOrderId(String orderId);

  int insertPaymentHistory(PaymentCreateDto payment);
}
