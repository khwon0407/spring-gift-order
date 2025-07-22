package gift.controller.wishlist;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.auth.JwtProvider;
import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WishlistControllerTest {
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
    @Order(1)
    void 위시리스트에_상품을_추가한다() {
        var request = new WishlistRequestDto(1L, 5L);
        var token = jwtProvider.createToken(new Member(2L, "user@user.com", "userpw", Role.USER));
        
        var response = restClient.post()
            .uri("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(WishlistResponseDto.class);
        
        assertThat(response.getProductName()).isEqualTo("아메리카노");
        assertThat(response.getProductCnt()).isEqualTo(5L);
    }
    
    @Test
    @Order(2)
    void 위시리스트를_조회한다() {
        var token = jwtProvider.createToken(new Member(2L, "user@user.com", "userpw", Role.USER));
        
        var response = restClient.get()
            .uri("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(WishlistResponseDto[].class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()[0].getProductCnt()).isEqualTo(5L);
    }
    
    @Test
    @Order(3)
    void 위시리스트에서_물품_개수를_수정한다() throws IOException {
        var request = new WishlistRequestDto(1L, 2L);
        var token = jwtProvider.createToken(new Member(2L, "user@user.com", "userpw", Role.USER));
        
        var response = restClient.patch()
            .uri("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(WishlistResponseDto.class);
        
        assertThat(response.getProductName()).isEqualTo("아메리카노");
        assertThat(response.getProductCnt()).isEqualTo(2L);
    }
    
    @Test
    @Order(4)
    void 위시리스트에서_물품을_삭제한다() throws IOException {
        var token = jwtProvider.createToken(new Member(2L, "user@user.com", "userpw", Role.USER));
        
        var response = restClient.delete()
            .uri("/api/wishlist/1")
            .header("Authorization", "Bearer " + token)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
}