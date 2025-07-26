# spring-gift-order

## step 1. 카카오 로그인
1. 카카오 어플리케이션 생성 및 설정
2. 카카오 공식 문서를 자세히 읽으며 "인가코드를 활용해 엑세스 토큰 발급"을 구현
3. 카카오 로그인 시도 연결을 링크로 간소화
4. 발급받은 Access code를 활용해 유저의 이메일을 얻어와 db에 등록 및 로그인

### 실행 시 주의 사항
1. application.properties의 kakao.cliend-id가 반드시 채워져 있어야 함. 고의적으로 your-REST_API_KEY 로 채워놓았기에, 반드시 본인의 유효한 key를 입력할 것.
2. localhost:8080/login/kakao 를 입력하면 자동으로 카카오 로그인 및 인가 코드 발행으로 연결됨. 카카오 로그인이 되어있을 경우 바로 엑세스 토큰 발급까지 이어짐.
3. 로그인 시 리턴되는 값은 멤버 jwt token만 리턴됨. access token은 유저 정보에 저장하는 식으로 구현.