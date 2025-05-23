package com.matdongsan.api.service;

import com.matdongsan.api.dto.notice.NoticeCreateRequest;
import com.matdongsan.api.dto.notice.NoticeDeleteRequest;
import com.matdongsan.api.dto.notice.NoticeGetRequest;
import com.matdongsan.api.dto.notice.NoticeUpdateRequest;
import com.matdongsan.api.mapper.NoticeMapper;
import com.matdongsan.api.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NoticeService {

  private final NoticeMapper mapper;

  /**
   * 공지사항 목록 조회
   * @param request 공지사항 검색 조건
   * @return 공지사항 목록
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getNoticeListWithPagination(NoticeGetRequest request) {
    List<NoticeVO> notices = mapper.selectNotices(request);
    int total = mapper.countNotices(request);
    return Map.of(
            "notices", notices,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 공지사항 상세 조회
   * @param id 공지사항 id
   * @return 공지사항 상세 데이터
   */
  @Transactional(readOnly = true)
  public NoticeVO getNoticeDetail(Long id) {
    return mapper.selectNoticeDetail(id);
  }

  /**
   * 공지사항 생성
   * @param request 공지사항 데이터
   * @return 생성된 공지사항 id
   */
  public Long createNotice(NoticeCreateRequest request) {
    return mapper.insertNotice(request);
  }

  /**
   * 공지사항 수정 (관리자)
   * @param request 수정 데이터
   * @return true or false
   */
  public boolean updateNotice(NoticeUpdateRequest request) {
    return mapper.updateNotice(request) > 0;
  }

  /**
   * 공지사항 삭제 (관리자)
   * @param request 삭제할 데이터
   * @return true or false
   */
  public boolean deleteNotice(NoticeDeleteRequest request) {
    return mapper.softDeleteNotice(request) > 0;
  }
}
