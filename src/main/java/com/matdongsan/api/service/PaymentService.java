
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
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PaymentService {

  private static final String TICKET_TYPE_RESERVATION = "reservation";
  private static final String TICKET_NAME_DEFAULT = "등록권";
  private static final String TICKET_NAME_RESERVATION = "대리권";
  private static final String ITEM_NAME_DEPOSIT = "예약금";
  private static final String PAYMENT_STATUS_COMPLETED = "결제완료";

  private final PaymentMapper mapper;

  /**
   * 임시 예약 생성 (결제 이전 단계)
   * @param request 예약 생성 요청 DTO
   * @return 임시 예약 정보 (orderId, tempId 포함)
   */
  public TempReservationVO createTempReservation(ReservationCreateDto request) {
    String orderId = generateOrderId();
    request.setOrderId(orderId);

    mapper.insertTempReservation(request);

    TempReservationVO vo = new TempReservationVO();
    vo.setOrderId(orderId);
    vo.setTempId(request.getId());
    return vo;
  }

  /**
   * 결제 후 예약 확정 및 결제 내역 저장
   * @param request 결제 확인 및 예약 확정 DTO
   * @return 확정된 예약 ID
   */
  public Long confirmReservation(ReservationConfirmDto request) {
    TempReservationVO temp = mapper.findTempByOrderId(request.getOrderId());

    validateTempReservation(temp, request.getAmount());

    ReservationCreateDto reservation = buildConfirmedReservation(temp);
    mapper.insertConfirmedReservation(reservation);

    PaymentCreateDto payment = buildPaymentInfo(request, temp);
    mapper.insertPaymentHistory(payment);

    mapper.deleteTempByOrderId(request.getOrderId());

    return reservation.getId();
  }

  /**
   * 등록권/대리권 구매 처리
   * @param request 구매 요청 DTO
   * @return 처리된 행 수
   */
  public Long purchaseTicket(PurchaseTicketDto request) {

    if (request.getUserId() == null) {
      throw new IllegalArgumentException("회원 정보가 누락되었습니다.");
    }
    if (request.getTicketId() == null || request.getTicketId().isBlank()) {
      throw new IllegalArgumentException("티켓 ID는 필수입니다.");
    }

    request.setOrderId(generateOrderId());
    request.setItemName(resolveTicketName(request.getTicketId()));

    int historyResult = mapper.createTicketPaymentHistory(request);
    int ticketResult = mapper.createPurchaseTicket(request);

    if (historyResult != 1 || ticketResult != 1) {
      throw new IllegalStateException("티켓 구매 처리 중 오류가 발생했습니다.");
    }
    return 2L;
  }

  /**
   * 티켓 정보 조회
   * @param ticketId 티켓 ID (nullable)
   * @return 티켓 정보 리스트
   */
  public List<TicketVO> getTicketInfoData(String ticketId) {
    return mapper.selectTicketInfoData(ticketId);
  }

  /**
   * 임시 예약 데이터 검증
   * @param temp 임시 예약 정보
   * @param expectedAmount 결제 금액
   */
  private void validateTempReservation(TempReservationVO temp, Long expectedAmount) {
    if (temp == null) {
      throw new IllegalArgumentException("해당 orderId에 대한 임시 예약이 존재하지 않습니다.");
    }

    if (!temp.getDeposit().equals(expectedAmount)) {
      throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
    }
  }

  /**
   * TempReservation 정보를 Reservation DTO로 변환
   * @param temp 임시 예약 정보
   * @return 예약 생성 DTO
   */
  private ReservationCreateDto buildConfirmedReservation(TempReservationVO temp) {
    ReservationCreateDto dto = new ReservationCreateDto();
    dto.setOrderId(temp.getOrderId());
    dto.setUserId(temp.getUserId());
    dto.setPropertyId(temp.getPropertyId());
    dto.setInfo(temp.getInfo());
    dto.setReservedDate(temp.getReservedDate());
    dto.setReservedTime(temp.getReservedTime());
    dto.setDeposit(temp.getDeposit());
    return dto;
  }

  /**
   * 결제 요청 정보를 기반으로 결제 DTO 생성
   * @param request 결제 확인 요청 DTO
   * @param temp 임시 예약 정보
   * @return 결제 생성 DTO
   */
  private PaymentCreateDto buildPaymentInfo(ReservationConfirmDto request, TempReservationVO temp) {
    PaymentCreateDto dto = new PaymentCreateDto();
    dto.setOrderId(temp.getOrderId());
    dto.setUserId(temp.getUserId());
    dto.setPaymentKey(request.getPaymentKey());
    dto.setAmount(request.getAmount());
    dto.setItemName(ITEM_NAME_DEPOSIT);
    dto.setStatus(PAYMENT_STATUS_COMPLETED);
    return dto;
  }

  /**
   * 티켓 타입에 따라 등록권/대리권 명칭 반환
   * @param ticketId 티켓 ID
   * @return 티켓 이름
   */
  private String resolveTicketName(String ticketId) {
    return TICKET_TYPE_RESERVATION.equals(ticketId) ? TICKET_NAME_RESERVATION : TICKET_NAME_DEFAULT;
  }

  /**
   * 주문 ID 생성 (UUID 기반 Base36)
   * @return 주문 ID 문자열
   */
  private String generateOrderId() {
    UUID uuid = UUID.randomUUID();
    long lsb = uuid.getLeastSignificantBits();
    return Long.toUnsignedString(lsb, 36).toUpperCase();
  }
}
