package com.matdongsan.api.service;

import com.matdongsan.api.dto.agent.AgentReportCreateRequest;
import com.matdongsan.api.dto.agent.AgentReportGetRequest;
import com.matdongsan.api.dto.agent.AgentReportGetResponse;
import com.matdongsan.api.dto.report.ReportCreateDto;
import com.matdongsan.api.mapper.ReportMapper;
import com.matdongsan.api.mapper.ReservationMapper;
import com.matdongsan.api.vo.ReportVO;
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
public class ReportService {

  private final ReportMapper mapper;
  private final ReservationMapper reservationMapper;

  /**
   * 중개인 신고
   * @param request 로그인 유저 id, 신고 유형, 중개인 id, 신고 내용
   */
  public void createAgentReport(AgentReportCreateRequest request) {
    // '회원'과 '중개인'이 예약 관련한 정보가 있는지 확인
    boolean hasReservation = reservationMapper.existsReservation(request);

    // 중개인 신고
    mapper.insertAgentReport(request);
  }

  /**
   * 로그인 사용자가 신고한 중개인 목록 조회
   * @param request 로그인 유저 id, 페이지, 사이즈,
   * @return 중개인 이름, 신고 유형, 신고 내용, 생성일자 Map
   */
  public Map<String, Object> getAgentReports(AgentReportGetRequest request) {
    List<AgentReportGetResponse> reports = mapper.selectReports(request);
    Integer total = mapper.countReports(request);

    return Map.of(
            "reports", reports,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 매물 신고 생성
   * @param request
   * @return
   */
  public Long createReport(ReportCreateDto request) {
    return mapper.insertProrpertyReport(request);
  }

  /**
   * 매물 신고
   * @param id 유저 id
   * @return 신고데이터
   */
  public List<ReportVO> getReportsByUser(Long id) {
    return mapper.selectReportsByUser(id);
  }
}
