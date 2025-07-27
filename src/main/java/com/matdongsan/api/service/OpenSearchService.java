package com.matdongsan.api.service;

import com.matdongsan.api.vo.PropertyVO;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;
import org.opensearch.client.opensearch.OpenSearchClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenSearchService {
  private final OpenSearchClient openSearchClient;

  // properties 인덱스에서 title에 keyword 포함된 도큐먼트 검색
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> searchProperties(String keyword) throws IOException {
    SearchResponse<Map> response = openSearchClient.search(s -> s
                    .index("properties")
                    .query(q -> q
                            .multiMatch(mm -> mm
                                    .fields("title", "description", "address", "address_detail", "category", "tags")
                                    .query(keyword)
                            )
                    ),
            Map.class
    );


    return response.hits().hits().stream()
            .map(Hit::source)
            .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> searchAgents(String keyword) throws IOException {
    SearchResponse<Map> response = openSearchClient.search(s -> s
            .index("agents")
            .query(q -> q
                    .multiMatch(m -> m
                            .fields(
                                    "agentName",
                                    "brand",
                                    "address",
                                    "addressDetail",
                                    "bio",
                                    "description",
                                    "licenseNumber"
                            )
                            .query(keyword)
                    )
            ), Map.class);


    return response.hits().hits().stream()
            .map(Hit::source)
            .collect(Collectors.toList());
  }

  public void indexProperty(Map<String, Object> propertyDoc) throws IOException {
    openSearchClient.index(i -> i
            .index("properties")
            .id(String.valueOf(propertyDoc.get("id"))) // id 필드는 반드시 필요
            .document(propertyDoc)
    );
  }

  // 중개인 색인(등록/수정)
  public void indexAgent(Map<String, Object> agentDoc) throws IOException {
    openSearchClient.index(i -> i
            .index("agents")
            .id(String.valueOf(agentDoc.get("id")))
            .document(agentDoc)
    );
  }
    /**
     * 매물 및 태그 정보를 OpenSearch properties 인덱스에 색인.
     */
    public void indexPropertyWithTags(PropertyVO propertyVO, List<String> tags) throws IOException {
      // 1. 색인 도큐먼트 구성 (property + tags)
      Map<String, Object> doc = new HashMap<>();
      doc.put("id", propertyVO.getId());
      doc.put("title", propertyVO.getTitle());
      doc.put("address", propertyVO.getAddress());
      doc.put("address_detail", propertyVO.getAddressDetail());
      doc.put("category", propertyVO.getCategory());
      doc.put("tags", tags); // 태그 리스트

      // 필요한 추가 필드가 있다면 아래에 계속 추가
       doc.put("price", propertyVO.getPrice());
       doc.put("thumbnailUrl", propertyVO.getThumbnailUrl());

      // 2. 색인 요청 (id를 OpenSearch doc_id로 사용)
      IndexResponse response = openSearchClient.index(i -> i
              .index("properties")
              .id(String.valueOf(propertyVO.getId()))
              .document(doc)
      );
      // 색인 결과(response) 필요시 추가 로직 작성 가능
    }
  }

