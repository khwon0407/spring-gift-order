package gift.repository.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    
    @Test
    void 저장() {
        Product product = new Product(null, "이름", 3000L, "None");
        
        var actual = productRepository.save(product);
        
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(product.getName())
        );
    }
    
    @Test
    void 검색() {
        Product product = new Product(null, "이름", 3000L, "None");
        
        var temp = productRepository.save(product);
        
        var actual = productRepository.findById(temp.getId()).get().getName();
        
        assertThat(actual).isEqualTo(product.getName());
    }
}