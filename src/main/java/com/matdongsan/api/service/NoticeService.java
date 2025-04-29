package com.matdongsan.api.service;

import com.matdongsan.api.dto.notice.NoticeRequest;
import com.matdongsan.api.mapper.NoticeMapper;
import com.matdongsan.api.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeMapper mapper;

  public List<NoticeVO> getNoticeList(NoticeRequest request) {
    return mapper.getNoticeList(request);
  }
}
