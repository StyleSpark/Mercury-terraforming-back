# Matdongsan (온라인 부동산 매물 사이트) - 백엔드

## 개요
이 레포지토리는 온라인 부동산 매물 사이트의 백엔드 API를 담당하는 Spring Boot 기반 서버입니다. 
매물 정보, 사용자 관리, 거래 처리 등 온라인 부동산 거래 사이트의 핵심 기능을 제공합니다.

## 주요기능
- 매물 목록 및 필터링: 카테고리, 위치에 따른 상품 검색 및 필터링
- 매물 기능: 매물 추가, 삭제, 위치
- 지도 기능(카카오 지도 api) : 매물 위치, 마커, 클러스터 표시
- 사용자 인증: 로그인 및 회원가입
- 댓글 기능: 댓글 추가, 삭제, 수정, 정렬
- **데이터베이스 성능 튜닝**: 인덱스 최적화 및 쿼리 개선을 통해 DB 부하 감소
- **보안 강화**: JWT 또는 OAuth 기반 인증 방식을 점검하고, 사용자 정보 보호를 위한 보안 정책 적용
- **대규모 트래픽 테스트**: 효율적인 코드 작성을 위한 대규모 테스트를 해보면 좋을 듯함
- **대규모 트래픽 테스트**: 효율적인 코드 작성을 위해 대규모 트래픽 환경에서 테스트 진행
- **서비스 코드 개선**: 복잡한 로직을 단순화하고 가독성을 높이도록 리팩토링 필요
- **매물 타입 확장**: 현재 원룸만 다루고 있으나, 다른 매물 타입도 지원하도록 기능 추가 및 개선
- **동시성 처리 기법 적용**: 동시 요청 처리 안정성을 확보하기 위한 개선 필요
- **PG 결제사 테스트 도입**: 결제 기능을 추가하고, 안정적인 결제 시스템 연동 테스트 수행
- **캐싱 도입 및 최적화**: Redis 등을 활용해 데이터 조회 성능 개선 및 부하 분산
- **검색 기능 개선**: Elasticsearch 도입을 고려하여 효율적인 검색 환경 구축



## 전체 DB 스키마
![image](https://github.com/user-attachments/assets/1b041f45-7c3b-4a84-adf3-f089c0343d2d)

## 시스템 아키텍처
![image](https://github.com/user-attachments/assets/b72ee163-2923-4cf0-a808-b91620f39a72)

## 메인 페이지
![image](https://github.com/user-attachments/assets/633a367e-eeb6-41a9-b5c8-c5ff9b3f4190)

## 매물 상세 페이지 (요청)
![image](https://github.com/user-attachments/assets/6a089b00-9d73-48ed-b5e8-b29ed32ee9e2)

## 매물 상세 페이지 (댓글 문의)
![image](https://github.com/user-attachments/assets/93ecd69d-28ed-492b-959e-d5d6a9be5fd0)

## 매물 리스트 페이지 
![image](https://github.com/user-attachments/assets/a086b7a5-02b0-4b67-8392-a802076411d2)

## 매물 등록 페이지
![image](https://github.com/user-attachments/assets/373e3e86-4039-4b5a-82a9-8936cf81a24d)

## 중개인 리스트 페이지
![image](https://github.com/user-attachments/assets/1cd1131c-cbc6-4413-866c-9b4c4763083a)

## 중개인 상세 페이지
![image](https://github.com/user-attachments/assets/5222cca6-0727-49e6-8722-c310bb1220a0)
