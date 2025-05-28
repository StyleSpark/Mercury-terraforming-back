package com.matdongsan.api.service;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageConversionService {

  /**
   * 기본 WebP 변환 (손실 압축, 품질 기본값)
   */
  public byte[] convertToWebP(MultipartFile file) throws IOException {
    return convertToWebP(file, WebpWriter.DEFAULT);
  }

  /**
   * WebP 변환 - 무손실 압축 모드
   */
  public byte[] convertToWebPLossless(MultipartFile file) throws IOException {
    return convertToWebP(file, WebpWriter.DEFAULT.withLossless());
  }

  /**
   * WebP 변환 - 품질 조절 가능 (0~100)
   */
  public byte[] convertToWebP(MultipartFile file, int quality) throws IOException {
    WebpWriter writer = WebpWriter.DEFAULT.withQ(quality);
    return convertToWebP(file, writer);
  }

  /**
   * WebP 변환 - 내부 처리 로직
   */
  private byte[] convertToWebP(MultipartFile file, WebpWriter writer) throws IOException {
    try {
      ImmutableImage image = ImmutableImage.loader().fromStream(file.getInputStream());
      return image.bytes(writer);
    } catch (Exception e) {
      log.error("WebP 변환 실패", e);
      throw new IOException("WebP 변환 중 오류 발생", e);
    }
  }
}
