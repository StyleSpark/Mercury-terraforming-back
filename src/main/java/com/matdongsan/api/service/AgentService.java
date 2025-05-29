package com.matdongsan.api.service;

import com.matdongsan.api.mapper.AgentMapper;
import com.matdongsan.api.vo.AgentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
