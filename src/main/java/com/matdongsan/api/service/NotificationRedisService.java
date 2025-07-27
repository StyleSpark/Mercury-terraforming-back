package com.matdongsan.api.service;

import com.matdongsan.api.vo.ReservationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationRedisService {

  private final RedisTemplate<String, Object> redisTemplate;
  private static final String NOTI_PREFIX = "agent:notifications:";

  public void pushNotification(Long agentId, ReservationVO vo) {
    redisTemplate.opsForList().leftPush(NOTI_PREFIX + agentId, vo);
  }

  public List<ReservationVO> getNotifications(Long agentId) {
    String key = NOTI_PREFIX + agentId;

    // Redis에서 알림 가져오기
    List<Object> rawList = redisTemplate.opsForList().range(key, 0, -1);

    // 역직렬화
    List<ReservationVO> result = rawList.stream()
            .filter(ReservationVO.class::isInstance)
            .map(o -> (ReservationVO) o)
            .toList();

    // 조회 후 읽음 처리 (삭제)
    redisTemplate.delete(key);

    return result;
  }
}

