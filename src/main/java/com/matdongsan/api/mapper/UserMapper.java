package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.user.UpdateUserDto;
import com.matdongsan.api.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
  List<UserVO> findAll();

  int insertUser(UserVO user);

  UserVO findByEmail(String email);

  boolean existsByEmail(String email);

  void insertUserWithPassword(UserVO user);

  int updateAgentStatus(Long userId);

  boolean existsByNickname(String nickname);

  UserVO selectUserDataById(Long id);

  String selectUserProfile(Long id);

  int updateUserProfile(UpdateUserDto request);

  boolean checkNotAgent(Long userId);
}