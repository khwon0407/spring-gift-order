package gift.controller.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.auth.JwtProvider;
import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.ProductResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import java.io.IOException;
import java.util.ArrayList;
import org.assertj.core.api.AssertionsForClassTypes;
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
class ProductControllerTest {
    
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
    void 정상적으로_상품을_등록한다() {
        var optionList = new ArrayList<OptionRequestDto>();
        optionList.add(new OptionRequestDto("test", 1L));
        var request = new AddProductRequestDto("테스트 상품", 1000L, "https://test.com/image.jpg", true, optionList);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.post()
            .uri("/api/products")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("테스트 상품");
    }
    
    @Test
    @Order(2)
    void 옳지_않은_상품명_추가를_시도한다() throws IOException {
        var optionList = new ArrayList<OptionRequestDto>();
        optionList.add(new OptionRequestDto("test", 1L));
        var request = new AddProductRequestDto("테스트:상품", 1000L, "https://test.com/image.jpg", true, optionList);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.post()
            .uri("/api/products")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
    }
    
    @Test
    @Order(3)
    void 정보_일부가_비어있는_상품_추가를_시도한다() throws IOException {
        var optionList = new ArrayList<OptionRequestDto>();
        optionList.add(new OptionRequestDto("test", 1L));
        var request = new AddProductRequestDto("테스트 상품", null, "https://test.com/image.jpg", true, optionList);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.post()
            .uri("/api/products")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
    }
    
    @Test
    @Order(4)
    void 너무_긴_상품명_추가를_시도한다() throws IOException {
        var optionList = new ArrayList<OptionRequestDto>();
        optionList.add(new OptionRequestDto("test", 1L));
        var request = new AddProductRequestDto("15자를넘겨야하는데뭐라고할까고민좀했음", 1000L, "https://test.com/image.jpg", true, optionList);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.post()
            .uri("/api/products")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
    }
    
    @Test
    @Order(5)
    void 올바른_카카오_상품명_추가를_시도한다() {
        var optionList = new ArrayList<OptionRequestDto>();
        optionList.add(new OptionRequestDto("test", 1L));
        var request = new AddProductRequestDto("카카오 테스트상품", 1000L, "https://test.com/image.jpg", true, optionList);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.post()
            .uri("/api/products")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("카카오 테스트상품");
    }
    
    @Test
    @Order(6)
    void 올바르지_않은_카카오_상품명_추가를_시도한다() throws IOException {
        var optionList = new ArrayList<OptionRequestDto>();
        optionList.add(new OptionRequestDto("test", 1L));
        var request = new AddProductRequestDto("카카오 테스트상품", 1000L, "https://test.com/image.jpg", false, optionList);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.post()
            .uri("/api/products")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(String.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo("MD와의 협의 후 사용 가능한 이름입니다.");
    }
    
    @Test
    @Order(7)
    void 상품_검색을_시도한다() {
        
        var response = restClient.get()
            .uri("/api/products/1")
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("아메리카노");
    }
    
    @Test
    @Order(8)
    void 전체_상품_검색을_시도한다() {
        
        var response = restClient.get()
            .uri("/api/products?page=0&size=2&criteria=id")
            .retrieve()
            .body(ProductResponseDto[].class);
        
        assertThat(response[0].getName()).isEqualTo("아메리카노");
    }
    
    @Test
    @Order(9)
    void 정상적으로_상품을_수정한다() {
        var request = new ModifyProductRequestDto("테스트 상품", 1000L, "https://test.com/image.jpg", true);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.put()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("테스트 상품");
    }
    
    @Test
    @Order(10)
    void 정상적으로_상품_정보_일부를_수정한다() {
        var request = new ModifyProductRequestDto("테스트 상품", null, null, true);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.patch()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("테스트 상품");
    }
    
    @Test
    @Order(11)
    void 잘못된_이름으로_상품을_수정한다() throws IOException {
        var request = new ModifyProductRequestDto("15자를넘겨야하는데뭐라고할까고민좀했음", 1000L, "https://test.com/image.jpg", true);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.put()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
    }
    
    @Test
    @Order(12)
    void 잘못된_이름으로_상품_정보_일부를_수정한다() throws IOException {
        var request = new ModifyProductRequestDto("15자를넘겨야하는데뭐라고할까고민좀했음", null, null, true);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.patch()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
    }
    
    @Test
    @Order(13)
    void 올바른_카카오_이름으로_상품을_수정한다() {
        var request = new ModifyProductRequestDto("카카오 테스트상품", 1000L, "https://test.com/image.jpg", true);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.put()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("카카오 테스트상품");
    }
    
    @Test
    @Order(14)
    void 올바르지_않은_카카오_이름으로_상품을_수정한다() throws IOException {
        var request = new ModifyProductRequestDto("카카오 테스트상품", 1000L, "https://test.com/image.jpg", false);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.put()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(String.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo("MD와의 협의 후 사용 가능한 이름입니다.");
    }
    
    @Test
    @Order(15)
    void 올바르지_않은_카카오_이름으로_상품_정보를_일부_수정한다() throws IOException {
        var request = new ModifyProductRequestDto("카카오 테스트상품", null, null, false);
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.patch()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(String.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo("MD와의 협의 후 사용 가능한 이름입니다.");
    }
    
    @Test
    @Order(16)
    void 정상적으로_상품을_삭제한다() throws IOException {
        var token = jwtProvider.createToken(new Member(1L, "admin@admin.com", "adminpw", Role.ADMIN));
        
        var response = restClient.delete()
            .uri("/api/products/1")
            .header("Authorization", "Bearer " + token)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
    
    @Test
    @Order(17)
    void 관리자가_아닌데_상품을_등록한다() {
        var optionList = new ArrayList<OptionRequestDto>();
        optionList.add(new OptionRequestDto("test", 1L));
        var request = new AddProductRequestDto("테스트 상품", 1000L, "https://test.com/image.jpg", true, optionList);
        var token = jwtProvider.createToken(new Member(2L, "user@user.com", "userpw", Role.USER));
        
        var response = restClient.post()
            .uri("/api/products")
            .header("Authorization", "Bearer " + token)
            .body(request)
            .exchange((req, res) -> {
                var status = res.getStatusCode();
                var body = res.bodyTo(String.class);
                return new ResponseEntity<>(body, status);
            });
        
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }
}