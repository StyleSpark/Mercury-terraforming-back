package com.matdongsan.api.service;

import com.matdongsan.api.dto.inquiry.QuestionCreateDto;
import com.matdongsan.api.dto.inquiry.QuestionDeleteDto;
import com.matdongsan.api.dto.inquiry.QuestionUpdateDto;
import com.matdongsan.api.mapper.QuestionMapper;
import com.matdongsan.api.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuestionService {

  final private QuestionMapper mapper;

  public int createQuestion(QuestionCreateDto request) {
    int result = 0;
    result += mapper.insertQuestion(request);
    return result;
  }

  public List<QuestionVO> getQuestionByUser(Long id) {
    return mapper.selectQuestionByUser(id);
  }

  public QuestionVO getQuestionById(Long id) {
    return mapper.selectQuestionById(id);  // Mapper에 selectQuestionById 쿼리 필요
  }

  public int updateQuestionByUser(QuestionUpdateDto request) {

    // 1️⃣ 본인 글인지 확인 필요 (select로 확인하거나 바로 update 후 affectedRows 확인)
    // 여기서는 간단히 바로 update 실행한다고 가정 -> 리팩토링때 추가 할 것같음

    //    if (affectedRows == 0) {
//      throw new CustomException(ErrorCode.NOT_FOUND_OR_FORBIDDEN); // 권한 오류 or 없는 데이터 처리
//    }

    return mapper.updateQuestionById(request); // 몇 개 수정되었는지 반환
  }

  public int deleteQuestionByUser(QuestionDeleteDto request) {
    return mapper.deleteQuestionByIdAndUserId(request);
  }
}
