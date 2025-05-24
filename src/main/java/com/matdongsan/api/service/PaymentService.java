package com.matdongsan.api.service;

import com.matdongsan.api.dto.payment.PaymentCreateDto;
import com.matdongsan.api.dto.payment.PurchaseTicketDto;
import com.matdongsan.api.dto.reservation.ReservationConfirmDto;
import com.matdongsan.api.dto.reservation.ReservationCreateDto;
import com.matdongsan.api.mapper.PaymentMapper;
import com.matdongsan.api.vo.TempReservationVO;
import com.matdongsan.api.vo.TicketVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PaymentService {

  final private PaymentMapper mapper;
  /**
   * 임시 예약 데이터 생성
   * @param request
   * @return
   */
  public TempReservationVO createTempReservation(ReservationCreateDto request) {
    String orderId = generateOrderId();
    request.setOrderId(orderId);

    mapper.insertTempReservation(request); // PK는 request.id에 자동 주입됨

    TempReservationVO vo = new TempReservationVO();
    vo.setOrderId(orderId);
    vo.setTempId(request.getId());
    return vo;
  }

  /**
   * 예약 확인 로직
   * 임시 상태인 예약 확정 및 payment history 기록 남김
   * @param request
   * @return
   */
  public Long confirmReservation(ReservationConfirmDto request) {
    // 1. temp 조회
    TempReservationVO temp = mapper.findTempByOrderId(request.getOrderId());
    if (temp == null) {
      throw new IllegalArgumentException("해당 orderId에 대한 임시 예약이 존재하지 않습니다.");
    }

    // 2. 위변조 방지
    if (!temp.getDeposit().equals(request.getAmount())) {
      throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
    }

    // 3. reservations insert
    ReservationCreateDto reservation = new ReservationCreateDto();
    reservation.setOrderId(temp.getOrderId());
    reservation.setUserId(temp.getUserId());
    reservation.setPropertyId(temp.getPropertyId());
    reservation.setInfo(temp.getInfo());
    reservation.setReservedDate(temp.getReservedDate());
    reservation.setReservedTime(temp.getReservedTime());
    reservation.setDeposit(temp.getDeposit());

    mapper.insertConfirmedReservation(reservation);

    // 4. payments insert
    PaymentCreateDto payment = new PaymentCreateDto();
    payment.setOrderId(temp.getOrderId());
    payment.setUserId(temp.getUserId());
    payment.setPaymentKey(request.getPaymentKey());
    payment.setAmount(request.getAmount());
    payment.setItemName("예약금");
    payment.setStatus("결제완료");

    mapper.insertPaymentHistory(payment);

    // 5. temp 삭제
    mapper.deleteTempByOrderId(request.getOrderId());

    return reservation.getId();
  }

  /**
   * OrderId uuid 생성기
   * @return
   */
  private String generateOrderId() {
    UUID uuid = UUID.randomUUID();
    long lsb = uuid.getLeastSignificantBits();
    return Long.toUnsignedString(lsb, 36).toUpperCase(); // 예: ORD-K4L9LJ2B8F3M
  }

  /**
   * 등록권 구매
   * @param request
   * @return
   */
  public Long purchaseTicket(PurchaseTicketDto request) {
    Long result = 0L;

    request.setOrderId(generateOrderId());

    String itemName = "ticket".equals(request.getTicketId()) ? "등록권" : "대리권";
    request.setItemName(itemName);

    result += mapper.createTicketPaymentHistory(request); // 구매 기록 생성
    result += mapper.createPurchaseTicket(request); // 티켓 구매
    return result;
  }

  public List<TicketVO> getTicketInfoData(String ticketId) {
    return mapper.selectTicketInfoData(ticketId);
  }
}
