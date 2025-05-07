package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.notice.NoticeCreateRequest;
import com.matdongsan.api.dto.notice.NoticeDeleteRequest;
import com.matdongsan.api.dto.notice.NoticeGetRequest;
import com.matdongsan.api.dto.notice.NoticeUpdateRequest;
import com.matdongsan.api.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
  List<NoticeVO> selectNotices(NoticeGetRequest request);

  NoticeVO selectNoticeDetail(Long id);

  Long insertNotice( NoticeCreateRequest request);

  int updateNotice(NoticeUpdateRequest request);

  int softDeleteNotice(NoticeDeleteRequest request);

  int countNotices(NoticeGetRequest request);
}
