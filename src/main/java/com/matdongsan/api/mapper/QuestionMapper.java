package com.matdongsan.api.mapper;

import com.matdongsan.api.dto.inquiry.QuestionCreateDto;
import com.matdongsan.api.dto.inquiry.QuestionDeleteDto;
import com.matdongsan.api.dto.inquiry.QuestionUpdateDto;
import com.matdongsan.api.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
  int insertQuestion(QuestionCreateDto request);

  List<QuestionVO> selectQuestionByUser(Long id);

  QuestionVO selectQuestionById(Long id);

  int updateQuestionById(QuestionUpdateDto request);

  int deleteQuestionByIdAndUserId(QuestionDeleteDto request);
}
