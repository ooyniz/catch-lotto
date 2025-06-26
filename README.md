![catchlotto-banner](https://github.com/user-attachments/assets/f03c6ca2-f803-41c9-b080-7740b389b255)

# 🎯 동행 로또 번호 추천 웹 애플리케이션

> 동행복권(lotto.dhlottery.co.kr)의 로또 당첨 번호를 크롤링하고, 이를 기반으로 로또 번호를 분석 및 추천하는 웹 애플리케이션입니다.  
> 랜덤 추천, 통계 기반 추천 방식으로 로또 번호를 예측하고 추천해줍니다.<br>
> Java(Spring Boot)와 React를 기반으로 제작되었으며, JWT 인증과 Redis 세션 캐싱, MySQL 데이터베이스를 사용합니다.

---
### 실행 화면
- 메인 화면
![image](https://github.com/user-attachments/assets/60fd74b4-b036-439a-ab69-e2e9f71918cf)

### 🎲 랜덤 추천 번호

> **랜덤 번호를 추천받을 수 있어요!**
![image](https://github.com/user-attachments/assets/ec98af1a-5691-4fe8-b169-e18717d7f430)

### 🎲 많이 나온 번호로 추천

> **최근 회차 기준 많이 등장한 번호 중에서 추천!**  
> 번호 통계를 확인하고 원하는 번호를 선택한 후 추천을 받을 수 있습니다.
![image](https://github.com/user-attachments/assets/fc8dda6d-f410-4b96-b150-82b0c906c9f4)

## 🛠️ 기술 스택

### 🔙 Backend
- **Java 17**
- **Spring Boot**
- **Spring Security + JWT** (Redis를 이용한 토큰 캐싱)
- **MySQL** (당첨 번호 및 유저 정보 저장)
- **Jsoup** (로또 당첨 번호 크롤링)

### 🔜 Frontend
- **React**
- **Axios** (API 통신)
---

## 🔐 인증 및 보안

- **JWT 기반 인증**
- **Redis**: 토큰 저장 및 세션 관리
- **Spring Security**: API 보안 처리

---

## 📦 주요 기능

| 기능 | 설명 |
|------|------|
| ✅ 로또 번호 크롤링 | 동행복권 공식 사이트에서 최신 당첨 번호 자동 수집 |
| ✅ 번호 추천 | 과거 데이터를 기반으로 추천 알고리즘 적용 |
| ✅ 회원가입 / 로그인 | JWT 기반 로그인, Redis 세션 관리 |


## 🚀 실행 방법

### 백엔드 서버 실행으로 프론트 자동 빌드 후 실행

```bash
./gradlew build
java -jar build/libs/catch-lotto-0.0.1.SNAPSHOT.jar
```
