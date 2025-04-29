package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.notice.NoticeRequest;
import com.matdongsan.api.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
  List<NoticeVO> getNoticeList(@Param("request") NoticeRequest request);
}
