package gift.repository.wishlist;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import gift.entity.WishlistInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WishlistRepositoryTest {
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Test
    void 저장() {
        WishlistInfo wishlistInfo = new WishlistInfo(
            null,
            new Member(2L, "user@user.com", "userpw", Role.USER),
            new Product(1L, "아메리카노", 4500L, "http://image.url.americano"),
            5L
        );
        
        var actual = wishlistRepository.save(wishlistInfo);
        
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getProductCnt()).isEqualTo(wishlistInfo.getProductCnt())
        );
    }
    
    @Test
    void 검색() {
        WishlistInfo wishlistInfo = new WishlistInfo(
            null,
            new Member(2L, "user@user.com", "userpw", Role.USER),
            new Product(1L, "아메리카노", 4500L, "http://image.url.americano"),
            5L
        );
        
        wishlistRepository.save(wishlistInfo);
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Direction.ASC, "id"));
        var actual = wishlistRepository.findAllByMemberId(2L, pageable).getContent().get(0).getProductCnt();
        
        assertThat(actual).isEqualTo(wishlistInfo.getProductCnt());
    }
}