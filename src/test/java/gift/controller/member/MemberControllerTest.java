package gift.controller.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberControllerTest {
    @LocalServerPort
    private int port;
    
    private RestClient restClient;
    
    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }
    
    @Test
    @Order(1)
    void 정상적으로_회원을_등록한다() {
        var request = new MemberRequestDto("test@gmail.com", "testpw1111");
        
        var responseEntity = restClient.post()
            .uri("/api/members/register")
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(MemberResponseDto.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(201); // 상태코드 201 확인
        assertThat(responseEntity.getBody()).isNotNull();
        AssertionsForClassTypes.assertThat(responseEntity.getBody().token()).isNotBlank(); // 예시: 토큰이 존재함을 검증
    }
    
    @Test
    @Order(2)
    void 정상적으로_회원_로그인한다() {
        var request = new MemberRequestDto("test@gmail.com", "testpw1111");
        
        var responseEntity = restClient.post()
            .uri("/api/members/login")
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(MemberResponseDto.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200); // 상태코드 201 확인
        assertThat(responseEntity.getBody()).isNotNull();
        AssertionsForClassTypes.assertThat(responseEntity.getBody().token()).isNotBlank(); // 예시: 토큰이 존재함을 검증
    }
    
    @Test
    @Order(3)
    void 중복된_회원을_등록한다() {
        var request = new MemberRequestDto("test@gmail.com", "testpw1111");
        
        var responseEntity = restClient.post()
            .uri("/api/members/register")
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(String.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(409);
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isNotNull();
    }
    
    @Test
    @Order(4)
    void 없는_회원으로_로그인한다() {
        var request = new MemberRequestDto("test1234@gmail.com", "testpw1111");
        
        var responseEntity = restClient.post()
            .uri("/api/members/login")
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(String.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(401);
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isNotNull();
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isNotBlank();
    }
}