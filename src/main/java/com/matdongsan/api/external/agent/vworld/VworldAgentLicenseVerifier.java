package com.matdongsan.api.external.agent.vworld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdongsan.api.dto.agent.AgentRegisterRequest;
import com.matdongsan.api.external.agent.config.VworldApiProperties;
import com.matdongsan.api.external.agent.verifier.AgentLicenseVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 중개업자 정보를 확인하는 객체
 * 국토 교통부 부동산중개업자정보조회 API 사용
 * (https://www.vworld.kr/dtna/dtna_apiSvcFc_s001.do?pageIndex=1&searchApiGbn=&searchGrpSeq=&searchKeyword=%EC%A4%91%EA%B0%9C&apiNum=44)
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class VworldAgentLicenseVerifier implements AgentLicenseVerifier {
  private final RestTemplate restTemplate;
  private final VworldApiProperties vworldApiProperties;

  @Override
  public boolean verify(AgentRegisterRequest request) {

    // 1. 검증 요청에 들어온 값 로그 확인
    log.info("[검증 요청] agentName={}, officeName={}, jurirno={}",
            request.getAgentName(), request.getOfficeName(), request.getJurirno());

    // 2. 요청 URL을 구성하기 위한 파라미터 인코딩 (중복 인코딩 방지용)
    String encodedOfficeName = UriUtils.encode(request.getOfficeName(), StandardCharsets.UTF_8);
    String encodedAgentName = UriUtils.encode(request.getAgentName(), StandardCharsets.UTF_8);
    String encodedJurirno = UriUtils.encode(request.getJurirno(), StandardCharsets.UTF_8);

    // 3. 요청 URI 구성 (인코딩된 값 사용 & build(false)로 추가 인코딩 방지)
    URI uri = URI.create(String.format(
            "%s?bsnmCmpnm=%s&brkrNm=%s&jurirno=%s&format=json&numOfRows=1&pageNo=1&key=%s&domain=localhost",
            vworldApiProperties.getBaseUrl(),
            encodedOfficeName,
            encodedAgentName,
            encodedJurirno,
            vworldApiProperties.getKey()
    ));

    log.info("[API 요청 URI] {}", uri);

    // 4. 요청 헤더 설정 (User-Agent 없으면 가끔 차단됨)
    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "Mozilla/5.0");

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    // 5. GET 요청 전송
    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

    // 6. 응답 본문 추출
    String responseBody = response.getBody();
    log.debug("[API 응답 원문] {}", responseBody);

    if (responseBody == null || responseBody.isEmpty()) {
      log.warn("[API 응답] 응답 본문이 null이거나 비어있습니다.");
      return false;
    }

    try {
      ObjectMapper objectMapper = new ObjectMapper();

      JsonNode root = objectMapper.readTree(responseBody);

      // 7. 응답 JSON 구조 확인: "EDBrokers" 키 존재 여부
      if (!root.has("EDBrokers")) {
        log.warn("[API 응답] 'EDBrokers' 키가 존재하지 않습니다. 실제 응답: {}", responseBody);
        return false;
      }

      JsonNode edBrokers = root.get("EDBrokers");

      // 8. "field" 배열 유효성 확인
      if (!edBrokers.has("field") || !edBrokers.get("field").isArray()) {
        log.warn("[API 응답] 'field'가 존재하지 않거나 배열이 아님. 응답={}", edBrokers.toPrettyString());
        return false;
      }

      JsonNode fields = edBrokers.get("field");
      if (fields.isEmpty()) {
        log.warn("[API 응답] 'field' 배열이 비어있습니다.");
        return false;
      }

      // 9. 첫 번째 field 비교
      JsonNode field = fields.get(0);

      String resName = field.get("brkrNm").asText();
      String resOffice = field.get("bsnmCmpnm").asText();
      String resJurirno = field.get("jurirno").asText();

      log.info("[응답 비교] 요청 vs 응답");
      log.info("agentName: {} vs {}", request.getAgentName(), resName);
      log.info("officeName: {} vs {}", request.getOfficeName(), resOffice);
      log.info("jurirno: {} vs {}", request.getJurirno(), resJurirno);

      // 10. 모든 정보가 일치하는지 검증
      return request.getAgentName().equals(resName) &&
              request.getOfficeName().equals(resOffice) &&
              request.getJurirno().equals(resJurirno);

    } catch (Exception e) {
      log.error("[API 응답 파싱 실패] {}", e.getMessage(), e);
      return false;
    }
  }
}