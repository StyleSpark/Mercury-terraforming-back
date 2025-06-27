package com.matdongsan.api;

import com.matdongsan.api.dto.reservation.ReservationConfirmDto;
import com.matdongsan.api.mapper.PaymentMapper;
import com.matdongsan.api.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MatdongsanApiApplicationTests {

//	@Autowired
//	private PaymentService paymentService;
//
//	@Autowired
//	private PaymentMapper mapper;
//
//	@Test
//	void testConcurrentReservation() throws InterruptedException {
//		ExecutorService executor = Executors.newFixedThreadPool(2);
//
//		mapper.deleteTestReservations("ORD-TEST-001");
//		mapper.deleteTestReservations("ORD-TEST-002");
//
//		mapper.insertTempReservationForTest("ORD-TEST-001", 1001L, 9001L,
//						LocalDate.of(2025, 7, 1), LocalTime.of(14, 0), 15000, "테스트용1");
//
//		mapper.insertTempReservationForTest("ORD-TEST-002", 1001L, 9002L,
//						LocalDate.of(2025, 7, 1), LocalTime.of(14, 0), 15000, "테스트용2");
//		Runnable task1 = () -> {
//			try {
//				ReservationConfirmDto req = new ReservationConfirmDto();
//				req.setOrderId("ORD-TEST-001"); // temp_reservation에 사전 등록된 ID
//				req.setPaymentKey(UUID.randomUUID().toString());
//				req.setAmount(15000L);
//				paymentService.confirmReservation(req);
//				System.out.println("예약 성공 1");
//			} catch (Exception e) {
//				System.out.println("예약 실패 1: " + e.getMessage());
//			}
//		};
//
//		Runnable task2 = () -> {
//			try {
//				ReservationConfirmDto req = new ReservationConfirmDto();
//				req.setOrderId("ORD-TEST-002");
//				req.setPaymentKey(UUID.randomUUID().toString());
//				req.setAmount(15000L);
//				paymentService.confirmReservation(req);
//				System.out.println("예약 성공 2");
//			} catch (Exception e) {
//				System.out.println("예약 실패 2: " + e.getMessage());
//			}
//		};
//
//		executor.submit(task1);
//		executor.submit(task2);
//
//		executor.shutdown();
//		executor.awaitTermination(10, TimeUnit.SECONDS);
//
//		// 검증: reservations 테이블에 해당 시간대 예약은 1건이어야 함
//		int count = mapper.countReservationsByPropertyAndTime(1001L, "2025-07-01", "14:00");
//		System.out.println("총 예약 건수: " + count);
//		Assertions.assertEquals(1, count, "동일한 시간에 중복 예약이 발생했습니다");
//	}
}
