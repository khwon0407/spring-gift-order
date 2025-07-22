package gift.controller.productoption;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.auth.JwtProvider;
import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductOptionControllerTest {
    
    @LocalServerPort
    private int port;
    
    private RestClient restClient;
    @Autowired
    private JwtProvider jwtProvider;
    
    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }
    
    @Test
    void 옵션_추가() {
        var request = new OptionRequestDto("테스트용 와 옵션이다", 100L);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.post()
            .uri("/api/products/1/options")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(OptionResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("테스트용 와 옵션이다");
    }
    
    @Test
    void 옵션_검색() {
        var response = restClient.get()
            .uri("/api/products/1/options")
            .retrieve()
            .body(OptionResponseDto[].class);
        
        assertThat(response[0].getName()).isEqualTo("기본 옵션");
    }
    
    @Test
    void 옵션_수정() {
        var request = new OptionRequestDto("기이본 옵션", 100L);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.put()
            .uri("/api/products/1/options/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(OptionResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("기이본 옵션");
    }
}