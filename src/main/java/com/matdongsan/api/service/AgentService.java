package com.matdongsan.api.service;

import com.matdongsan.api.dto.agent.AgentGetRequest;
import com.matdongsan.api.dto.agent.AgentGetResponse;
import com.matdongsan.api.mapper.AgentMapper;
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

  private final AgentMapper mapper;

  /**
   * 중개인 단일 조회
   * @param agentId 중개인 고유 id
   * @return 중개인 데이터
   */
  @Transactional(readOnly = true)
  public AgentVO getAgentDetail(Long agentId) {
    return mapper.selectAgentDetail(agentId);
  }

  /**
   * 중개인 목록 조회
   * @param request 지역:address, 매물명:propertyName, 매물 유형:propertyType, 중개인 이름:agentName, 브랜드명:brandName
   * @return AgentGetResponse, 검색 결과 총 갯수, 페이지, 페이지 사이즈
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getAgentListWithPagination(AgentGetRequest request) {
    List<AgentGetResponse> agents = mapper.selectAgents(request);
    Integer total = mapper.countAgents(request);

    return Map.of(
            "agents", agents,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

}
