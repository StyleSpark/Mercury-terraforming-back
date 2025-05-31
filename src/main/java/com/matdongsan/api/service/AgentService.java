package com.matdongsan.api.service;

import com.matdongsan.api.dto.agent.AgentGetRequest;
import com.matdongsan.api.dto.agent.AgentGetResponse;
import com.matdongsan.api.dto.agent.AgentRegisterRequest;
import com.matdongsan.api.external.agent.verifier.AgentLicenseVerifier;
import com.matdongsan.api.mapper.AgentMapper;
import com.matdongsan.api.mapper.UserMapper;
import com.matdongsan.api.vo.AgentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AgentService {

  private final AgentMapper agentMapper;
  private final UserMapper userMapper;
  private final AgentLicenseVerifier licenseVerifier;

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
  public void registerAgent(AgentRegisterRequest request) {
    // 중개인인지 확인 (외부 API를 호출하여 자동으로 승인하는 방향으로 설계)
    if(!licenseVerifier.verify(request)) {
      throw new IllegalArgumentException("공인중개사 정보가 확인되지 않았습니다.");
    }

    // Agent 등록
    agentMapper.insertAgent(request);

    // 사용자 상태 변경
    userMapper.updateAgentStatus(request.getUserId());
  }
}
