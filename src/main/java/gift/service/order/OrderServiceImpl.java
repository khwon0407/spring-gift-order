package gift.service.order;

import gift.dto.api.order.OrderRequestDto;
import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.ProductOption;
import gift.entity.WishlistInfo;
import gift.exception.badrequest.WrongOptionQuantityException;
import gift.exception.notfound.NoOptionInfoException;
import gift.repository.order.OrderRepository;
import gift.repository.productoption.ProductOptionRepository;
import gift.repository.wishlist.WishlistRepository;
import gift.service.productoption.ProductOptionService;
import gift.service.wishlist.WishlistService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final ProductOptionRepository optionRepository;
    private final WishlistRepository wishlistRepository;
    private final ProductOptionService optionService;
    private final WishlistService wishlistService;
    
    private final OrderRepository orderRepository;
    
    public OrderServiceImpl(ProductOptionRepository optionRepository,
        WishlistRepository wishlistRepository, ProductOptionService optionService,
        WishlistService wishlistService, OrderRepository orderRepository) {
        this.optionRepository = optionRepository;
        this.wishlistRepository = wishlistRepository;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.orderRepository = orderRepository;
    }
    
    @Override
    @Transactional
    public OrderResponseDto orderProduct(OrderRequestDto requestDto, Member user) {
        // 상품 옵션 조회 및 주문 가능 여부 확인, 가능 시 개수 차감
        ProductOption orderOption = optionRepository.findById(requestDto.optionId())
            .orElseThrow(NoOptionInfoException::new);
        
        if(orderOption.getQuantity() <= requestDto.quantity()) {
            throw new WrongOptionQuantityException();
        }
        
        optionService.decreaseOptionQuantity(orderOption.getId(), requestDto.quantity());
        
        //상품 조회 및 위시리스트 포함 여부 확인, 삭제 진행
        Product product = orderOption.getProduct();
        
        Optional<WishlistInfo> wishlistInfo = wishlistRepository.findByMemberIdAndProductId(
            user.getId(), product.getId()
        );
        
        if(wishlistInfo.isPresent()) {
            wishlistService.deleteFromMyWishlist(user, product.getId());
        }
        
        //실제 주문 기록 저장
        LocalDateTime now = LocalDateTime.now();
        Order newOrder = new Order(null, requestDto.optionId(), requestDto.quantity(), now,
            requestDto.message());
        Order saved = orderRepository.save(newOrder);
        
        return new OrderResponseDto(
            saved.getId(), saved.getOptionId(), saved.getQuantity(),
            saved.getOrderDateTime(), saved.getMessage()
        );
    }
}
