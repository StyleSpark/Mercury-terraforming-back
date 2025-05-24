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

  final private FavoriteMapper mapper;

  public int addPropertyFavorite(PropertyFavoriteCreateDto request) {
    return  mapper.createPropertyFavorite(request);
  }

  public int removePropertyFavorite(PropertyFavoriteCreateDto request) {
    return mapper.deletePropertyFavorite(request);
  }
}
