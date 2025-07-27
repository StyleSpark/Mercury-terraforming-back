package com.matdongsan.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final StringRedisTemplate redisTemplate;

  public void saveRefreshToken(Long userId, String token, long expirationMillis) {
    String key = "refreshToken:" + userId;
    redisTemplate.opsForValue().set(key, token, Duration.ofMillis(expirationMillis));
  }

  public String getRefreshToken(Long userId) {
    return redisTemplate.opsForValue().get("refreshToken:" + userId);
  }

  public void deleteRefreshToken(Long userId) {
    redisTemplate.delete("refreshToken:" + userId);
  }
}

