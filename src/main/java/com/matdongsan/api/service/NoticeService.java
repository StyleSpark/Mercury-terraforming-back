package com.matdongsan.api.service;

import com.matdongsan.api.dto.notice.NoticeCreateRequest;
import com.matdongsan.api.dto.notice.NoticeDeleteRequest;
import com.matdongsan.api.dto.notice.NoticeGetRequest;
import com.matdongsan.api.dto.notice.NoticeUpdateRequest;
import com.matdongsan.api.mapper.NoticeMapper;
import com.matdongsan.api.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeMapper mapper;

  /**
   * 공지사항 목록 조회
   * @param request 공지사항 검색 조건
   * @return 공지사항 목록
   */
  public List<NoticeVO> getNoticeList(NoticeGetRequest request) {
    return mapper.selectNotices(request);
  }

  /**
   * 공지사항 상세 조회
   * @param id 공지사항 id
   * @return 공지사항 상세 데이터
   */
  public NoticeVO getNoticeDetail(Long id) {
    return mapper.selectNoticeDetail(id);
  }

  /**
   * 공지사항 생성
   * @param request 공지사항 데이터
   * @return 생성된 공지사항 id
   */
  public Long createNotice(NoticeCreateRequest request) {
    mapper.insertNotice(request);
    return request.getId();
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
