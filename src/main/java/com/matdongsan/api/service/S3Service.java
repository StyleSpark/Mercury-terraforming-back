package com.matdongsan.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String bucket;

  @Value("${aws.region}")
  private String region;

  // 기존 MultipartFile용 메서드
  public String uploadFile(String keyName, MultipartFile file) throws IOException {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(keyName)
            .contentType(file.getContentType())
            .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
    return getFileUrl(keyName);
  }

  // ✅ 새로 추가할 바이트 배열 업로드 메서드
  public String uploadBytes(String keyName, byte[] bytes, String contentType) {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(keyName)
            .contentType(contentType)
            .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
    return getFileUrl(keyName);
  }

  public String getFileUrl(String keyName) {
    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, keyName);
  }
}


