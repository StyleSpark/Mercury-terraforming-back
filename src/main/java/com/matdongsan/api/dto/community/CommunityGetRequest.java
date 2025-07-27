package com.matdongsan.api.dto.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "커뮤니티 게시글 목록 조회 요청 DTO")
public class CommunityGetRequest {

  @Schema(description = "커뮤니티 카테고리 ID", example = "1")
  private Long categoryId;

  @Schema(description = "커뮤니티 게시글 작성자 이름", example = "홍길동")
  private String userName;

  @Schema(description = "검색할 커뮤니티 게시글 제목 키워드", example = "원룸")
  private String title;

  @Schema(description = "검색할 커뮤니티 게시글 내용 키워드", example = "자취")
  private String content;

  @Schema(description = "검색할 커뮤니티 게시글 제목+내용 키워드", example = "매물")
  private String titleOrContent;

  @Schema(description = "커뮤니티 게시글 순서 필터", example = "latest(최신순) or views(조회수순)", defaultValue = "'latest'")
  private String sort;

  @Schema(description = "조회할 페이지 번호 (1부터 시작)", example = "1", defaultValue = "1")
  private int page = 1;

  @Schema(description = "페이지당 데이터 수", example = "10", defaultValue = "10")
  private int size = 10;

  public int getOffset() {
    return (page - 1) * size;
  }
}
