package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishlistInfo;
import gift.exception.badrequest.WrongCriteriaException;
import gift.exception.badrequest.WrongOrderException;
import gift.exception.notfound.NoProductInfoException;
import gift.exception.notfound.NotInWishlistException;
import gift.repository.product.ProductRepository;
import gift.repository.wishlist.WishlistRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistServiceImpl implements WishlistService {
    
    private static final List<String> VALID_CRITERIA = List.of("id", "productId", "productCnt");
    
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    
    public WishlistServiceImpl(ProductRepository productRepository,
        WishlistRepository wishlistRepository) {
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
    }
    
    @Override
    @Transactional
    public WishlistResponseDto addToMyWishlist(Member user, WishlistRequestDto requestDto) {
        
        Product product = productRepository.findById(requestDto.productId()).orElseThrow(
            NoProductInfoException::new);
        
        WishlistInfo saved = wishlistRepository.save(
            new WishlistInfo(null, user, product, requestDto.productCnt())
        );
        
        return new WishlistResponseDto(saved);
    }
    
    @Override
    public List<WishlistResponseDto> findMyWishlist(Member user, int pageNo, int pageSize, String criteria, String order) {
        if (!VALID_CRITERIA.contains(criteria)) {
            throw new WrongCriteriaException();
        }
        
        if(!order.equals("ASC") && !order.equals("DESC")) {
            throw new WrongOrderException();
        }
        
        var orderValue = order.equals("ASC") ? Direction.ASC : Direction.DESC;
        
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orderValue, criteria));
        
        Page<WishlistResponseDto> page = wishlistRepository.findAllByMemberId(user.getId(), pageable)
            .map(wishlist -> new WishlistResponseDto(
                wishlist.getProduct().getId(),
                wishlist.getProduct().getName(),
                wishlist.getProductCnt()
            ));
        return page.getContent();
    }
    
    @Override
    @Transactional
    public void deleteFromMyWishlist(Member user, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoProductInfoException::new);
        
        WishlistInfo wishlistInfo = wishlistRepository.findByMemberIdAndProductId(user.getId(), product.getId()).orElseThrow(
            NotInWishlistException::new);
        
        wishlistRepository.deleteByMemberIdAndProductId(wishlistInfo.getMember().getId(), wishlistInfo.getProduct().getId());
    }
    
    @Override
    @Transactional
    public WishlistResponseDto modifyProductCntFromMyWishlist(Member user,
        WishlistRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.productId())
            .orElseThrow(NoProductInfoException::new);
        
        WishlistInfo wishlistInfo = wishlistRepository.findByMemberIdAndProductId(user.getId(), product.getId())
            .orElseThrow(NotInWishlistException::new);
        
        if(requestDto.productCnt() == 0) {
            deleteFromMyWishlist(wishlistInfo.getMember(), wishlistInfo.getProduct().getId());
            return null;
        }
        
        wishlistInfo.changeProductCnt(requestDto.productCnt());
        
        return new WishlistResponseDto(wishlistRepository.save(wishlistInfo));
    }
}
