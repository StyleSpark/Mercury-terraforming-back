package com.matdongsan.api.service;

import com.matdongsan.api.mapper.FailedUploadMapper;
import com.matdongsan.api.mapper.PropertyImagesMapper;
import com.matdongsan.api.vo.FailedUploadVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeferredUploaderService {

  private final FailedUploadMapper failedUploadMapper;
  private final S3Service s3Service;
  private final PropertyImagesMapper imagesMapper;

  public void retryFailedUpload(FailedUploadVO fail) {
    try {
      s3Service.uploadBytes(fail.getKey(), fail.getFileData(), fail.getContentType());

      if (fail.getPropertyId() != null && fail.getUrl() != null) {
        imagesMapper.insertPropertyImage(fail.getPropertyId(), fail.getUrl());
      }

      failedUploadMapper.deleteById(fail.getId()); // 성공 시 삭제

    } catch (Exception e) {
      // 실패 시 그냥 테이블에 남겨두고 재시도
      log.error("재시도 업로드 실패: {}", fail.getKey(), e);
    }
  }
}
