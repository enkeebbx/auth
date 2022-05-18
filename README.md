#### 스택
- 스프링부트 자바
- 스프링 시큐리티
- H2
- 스웨거

#### 구현스펙 (구현완료)
- OTP 인증 및 ACK
- 회원가입 (OTP 인증 필요)
- 로그인 (이메일, 전화번호, 아이디 택 1)
- 비밀번호 재설정 (OTP 인증 필요)
- 프로필 조회 (로그인 필요)

#### 실행방법
1. IDE 에서 프로젝트 오픈후 실행
2. 로그에서 8080 포트로 실행 확인 (Tomcat started on port(s): 8080 (http) with context path '')
3. 스웨거 (주소 http://localhost:8080/swagger-ui.html) 를 통해 api 피들링이 가능합니다.
4. OTP 인증이 필요한 엔드포인트는 /api/otp/sms 와 /api/otp/confirm 호출 이후에 시도해주세요.
5. 프로필 조회는 로그인 api 에서 jwt 토큰을 받아 Authorization 박스에 Bearer [jwt 토큰] 을 넣어주세요.
예) Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFkYnNkbmQiLCJpYXQiOjE2NTI4Mjk1MjYsImV4cCI6MTY1MjkxNTkyNn0.7NRXCCxk9e1Z40yL3IqowM3ePpgfGu2tgy1K7iXfdS3fmCDplN_SGdUKxRuXbI0s6_OvgH9mODuq1HLzN7m-VA

#### 리뷰포인트
1. 스프링 시큐리티를 활용해서 jwt 로직을 어플리케이션 비즈니스 로직에서 최대한 분리했습니다.
2. 멀티모듈 프로젝트 구조로 구성했으며 api, database, client (서드파티) 모듈로 나눴습니다.
