package com.matdongsan.api.service;

import com.matdongsan.api.mapper.CommunityMapper;
import com.matdongsan.api.vo.CommunityVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityService {

  private final CommunityMapper mapper;

  public CommunityVO getCommunityById(Long id) {
    return mapper.getCommunityById(id);
  }

}
