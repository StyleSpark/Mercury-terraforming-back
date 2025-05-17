package com.matdongsan.api.mapper;

import com.matdongsan.api.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
  List<UserVO> findAll();

  int insertUser(UserVO user);

  UserVO findByEmail(String email);
}