package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.payment.PaymentCreateDto;
import com.matdongsan.api.dto.payment.PurchaseTicketDto;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.vo.TempReservationVO;
import com.matdongsan.api.vo.TicketVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {
  // 임시 예약 데이터 생성
  int insertTempReservation(ReservationCreateDto request);

  // orderId에 따른 임시 예약데이터
  TempReservationVO findTempByOrderId(String orderId);

  // 예약 확인 생성
  int insertConfirmedReservation(ReservationCreateDto reservation);

  // orderId에 따른 임시 삭제 
  int deleteTempByOrderId(String orderId);

  // 구매 기록 생성
  int insertPaymentHistory(PaymentCreateDto payment);
  
  // 등록권 구매 기록
  Long createTicketPaymentHistory(PurchaseTicketDto request);
  // 등록권 생성
  Long createPurchaseTicket(PurchaseTicketDto request);

  List<TicketVO> selectTicketInfoData(String ticketId);

  int consumeTicket(Long userId);
}
