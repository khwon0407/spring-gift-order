package gift.repository.productoption;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Product;
import gift.entity.ProductOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductOptionRepositoryTest {
    @Autowired
    ProductOptionRepository productOptionRepository;
    
    @Test
    void 저장() {
        Product product = new Product(1L, "아메리카노", 4500L, "http://image.url.americano");
        ProductOption productOption = new ProductOption(null, "이름", 3000L, product);
        
        var actual = productOptionRepository.save(productOption);
        
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(productOption.getName())
        );
    }
    
    @Test
    void 검색() {
        Product product = new Product(1L, "아메리카노", 4500L, "http://image.url.americano");
        ProductOption productOption = new ProductOption(null, "이름", 3000L, product);
        
        var temp = productOptionRepository.save(productOption);
        
        var actual = productOptionRepository.findById(temp.getId()).get().getName();
        
        assertThat(actual).isEqualTo(productOption.getName());
    }
}