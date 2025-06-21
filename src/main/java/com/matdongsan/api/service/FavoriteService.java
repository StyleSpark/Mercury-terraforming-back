package com.matdongsan.api.service;

import com.matdongsan.api.dto.favorite.PropertyFavoriteCreateDto;
import com.matdongsan.api.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FavoriteService {

  private final FavoriteMapper favoriteMapper;

  /**
   * 매물 즐겨찾기 추가
   * @param request 사용자 ID와 매물 ID
   * @return 처리 결과 (1: 성공, 0: 실패)
   */
  public int addPropertyFavorite(PropertyFavoriteCreateDto request) {
    return favoriteMapper.createPropertyFavorite(request);
  }

  /**
   * 매물 즐겨찾기 제거
   * @param request 사용자 ID와 매물 ID
   * @return 처리 결과 (1: 성공, 0: 실패)
   */
  public int removePropertyFavorite(PropertyFavoriteCreateDto request) {
    return favoriteMapper.deletePropertyFavorite(request);
  }
}
