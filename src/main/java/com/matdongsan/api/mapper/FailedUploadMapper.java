package com.matdongsan.api.mapper;

import com.matdongsan.api.vo.FailedUploadVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FailedUploadMapper {
  int insertFailedUpload(FailedUploadVO upload);
  List<FailedUploadVO> selectAll();
  int deleteById(Long id);
}
