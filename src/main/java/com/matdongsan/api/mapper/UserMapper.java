package com.matdongsan.api.mapper;

import com.matdongsan.api.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
  List<User> findAll();
}