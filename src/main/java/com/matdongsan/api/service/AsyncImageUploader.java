package com.matdongsan.api.service;


import com.matdongsan.api.dto.upload.DeferredUpload;
import com.matdongsan.api.mapper.FailedUploadMapper;
import com.matdongsan.api.mapper.PropertyImagesMapper;
import com.matdongsan.api.vo.FailedUploadVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncImageUploader {

  private final S3Service s3Service;
  private final PropertyImagesMapper imagesMapper;
  private final FailedUploadMapper failedUploadMapper;
  private final DeferredUploaderService deferredUploaderService; // 추가됨

  @Async
  public void upload(DeferredUpload upload) {
    try {
      s3Service.uploadBytes(upload.getKey(), upload.getData(), upload.getContentType());

      if (upload.getPropertyId() != null && upload.getUrl() != null) {
        imagesMapper.insertPropertyImage(upload.getPropertyId(), upload.getUrl());
      }

    } catch (Exception e) {
      log.error("비동기 이미지 업로드 실패: {}", upload.getKey(), e);

      // 실패 시 테이블에 저장
      FailedUploadVO fail = new FailedUploadVO();
      fail.setPropertyId(upload.getPropertyId());
      fail.setKey(upload.getKey());
      fail.setUrl(upload.getUrl());
      fail.setContentType(upload.getContentType());
      fail.setFileData(upload.getData());

      failedUploadMapper.insertFailedUpload(fail);

      // 즉시 재시도
      deferredUploaderService.retryFailedUpload(fail);
    }
  }
}
