package gift.repository.wishlist;

import gift.entity.WishlistInfo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistInfo, Long> {
    Optional<WishlistInfo> findByMemberIdAndProductId(Long memberId, Long productId);
    
    Page<WishlistInfo> findAllByMemberId(Long memberId, Pageable pageable);
    
    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}
