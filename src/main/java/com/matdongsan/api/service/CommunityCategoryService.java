package com.matdongsan.api.service;

import com.matdongsan.api.dto.community.category.CategoryCreateRequest;
import com.matdongsan.api.dto.community.category.CategoryDeleteRequest;
import com.matdongsan.api.dto.community.category.CategoryGetRequest;
import com.matdongsan.api.dto.community.category.CategoryUpdateRequest;
import com.matdongsan.api.mapper.CommunityCategoryMapper;
import com.matdongsan.api.vo.CommunityCategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommunityCategoryService {

  private final CommunityCategoryMapper mapper;

  /**
   * 커뮤니티 카테고리 목록 조회 (사용자 모두 가능)
   * @param request 카테고리명, 설명, 생성일자
   * @return id, 카테고리명, 설명, 생성일자
   */
  @Transactional(readOnly = true)
  public List<CommunityCategoryVO> getCommunityCategories(CategoryGetRequest request) {
    return mapper.selectCommunityCategories(request);
  }

  /**
   * 커뮤니티 카테고리 등록 (관리자)
   * @param request id, 카테고리명, 설명
   * @return 커뮤니티 카테고리 id
   */
  public Long createCommunityCategory(CategoryCreateRequest request) {
    mapper.insertCommunityCategory(request);
    return request.getId();
  }

  /**
   * 커뮤니티 카테고리 수정 (관리자)
   * @param request id, 카테고리명, 설명
   */
  public void updateCommunityCategory(CategoryUpdateRequest request) {
    mapper.updateCommunityCategory(request);
  }

  /**
   * 커뮤니티 카테고리 삭제 (관리자)
   * @param request id, 카테고리명
   */
  public void deleteCommunityCategory(CategoryDeleteRequest request) {
    mapper.softDeleteCommunityCategory(request);
  }
}
