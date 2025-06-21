package com.matdongsan.api.service;

import com.matdongsan.api.dto.notice.*;
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

  private final NoticeMapper noticeMapper;

  /**
   * 공지사항 목록 조회 (페이징)
   *
   * @param request 검색 조건 (제목, 노출 여부 등)
   * @return 공지사항 리스트 + 총 개수 + 페이징 정보
   */
  @Transactional(readOnly = true)
  public Map<String, Object> getNoticeListWithPagination(NoticeGetRequest request) {
    List<NoticeVO> notices = noticeMapper.selectNotices(request);
    int total = noticeMapper.countNotices(request);

    return Map.of(
            "notices", notices,
            "total", total,
            "page", request.getPage(),
            "size", request.getSize()
    );
  }

  /**
   * 공지사항 상세 조회
   *
   * @param id 공지사항 ID
   * @return 공지사항 단건 정보
   */
  @Transactional(readOnly = true)
  public NoticeVO getNoticeDetail(Long id) {
    return noticeMapper.selectNoticeDetail(id);
  }

  /**
   * 공지사항 등록
   *
   * @param request 제목, 내용, 작성자 등
   * @return 생성된 공지사항 ID
   */
  public Long createNotice(NoticeCreateRequest request) {
    return noticeMapper.insertNotice(request);
  }

  /**
   * 공지사항 수정
   *
   * @param request 공지사항 ID, 수정 내용 등
   * @return 성공 여부
   */
  public boolean updateNotice(NoticeUpdateRequest request) {
    return noticeMapper.updateNotice(request) > 0;
  }

  /**
   * 공지사항 삭제 (soft delete)
   *
   * @param request 공지사항 ID
   * @return 성공 여부
   */
  public boolean deleteNotice(NoticeDeleteRequest request) {
    return noticeMapper.softDeleteNotice(request) > 0;
  }
}
