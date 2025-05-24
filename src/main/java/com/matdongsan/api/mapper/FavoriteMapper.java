package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.favorite.PropertyFavoriteCreateDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper {
  int createPropertyFavorite(PropertyFavoriteCreateDto request);

  int deletePropertyFavorite(PropertyFavoriteCreateDto request);
}
