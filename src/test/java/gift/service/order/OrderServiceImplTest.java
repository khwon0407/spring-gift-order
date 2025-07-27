package gift.service.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.dto.api.order.OrderRequestDto;
import gift.dto.api.order.OrderResponseDto;
import gift.entity.*;
import gift.exception.badrequest.WrongOptionQuantityException;
import gift.exception.notfound.NoOptionInfoException;
import gift.repository.order.OrderRepository;
import gift.repository.productoption.ProductOptionRepository;
import gift.repository.wishlist.WishlistRepository;
import gift.service.productoption.ProductOptionService;
import gift.service.wishlist.WishlistService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private ProductOptionRepository optionRepository;
    
    @Mock
    private WishlistRepository wishlistRepository;
    
    @Mock
    private ProductOptionService optionService;
    
    @Mock
    private WishlistService wishlistService;
    
    @Mock
    private OrderRepository orderRepository;
    
    @InjectMocks
    private OrderServiceImpl orderService;
    
    private Member member;
    private Product product;
    private ProductOption productOption;
    
    @BeforeEach
    void setUp() {
        member = new Member(1L, "email@email.com", "pwpw", Role.USER);
        
        product = new Product(1L, "test", 5000L, "image");
        
        productOption = new ProductOption(1L, "testOption", 10L, product);
    }
    
    @Test
    void 정상_주문() {
        // given
        OrderRequestDto requestDto = new OrderRequestDto(1L, 2L, "테스트 메시지");
        Order savedOrder = new Order(1L, 1L, 2L, LocalDateTime.now(), "테스트 메시지");
        
        when(optionRepository.findById(1L)).thenReturn(Optional.of(productOption));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(wishlistRepository.findByMemberIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        
        // when
        OrderResponseDto response = orderService.orderProduct(requestDto, member);
        
        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(2, response.getQuantity());
        verify(optionService, times(1)).decreaseOptionQuantity(1L, 2L);
        verify(wishlistService, never()).deleteFromMyWishlist(any(), any());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    void 위시리스트_물건의_정상_주문() {
        // given
        OrderRequestDto requestDto = new OrderRequestDto(1L, 2L, "테스트 메시지");
        Order savedOrder = new Order(1L, 1L, 2L, LocalDateTime.now(), "테스트 메시지");
        WishlistInfo wishlistInfo = new WishlistInfo(1L, member, product, 1L); // 가짜 위시리스트 데이터
        
        when(optionRepository.findById(1L)).thenReturn(Optional.of(productOption));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(wishlistRepository.findByMemberIdAndProductId(1L, 1L)).thenReturn(Optional.of(wishlistInfo));
        
        // when
        OrderResponseDto response = orderService.orderProduct(requestDto, member);
        
        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(2, response.getQuantity());
        verify(optionService, times(1)).decreaseOptionQuantity(1L, 2L);
        verify(wishlistService, times(1)).deleteFromMyWishlist(eq(member), eq(1L));
        verify(orderRepository, times(1)).save(any(Order.class));
    }
    
    @Test
    void 수량_부족으로_인한_주문실패() {
        // given
        OrderRequestDto requestDto = new OrderRequestDto(200L, 11L, "테스트 메시지");
        
        when(optionRepository.findById(200L)).thenReturn(Optional.of(productOption));
        
        // when & then
        assertThrows(WrongOptionQuantityException.class, () ->
            orderService.orderProduct(requestDto, member));
    }
    
    @Test
    void 없는_옵션에_대한_주문실패() {
        // given
        OrderRequestDto requestDto = new OrderRequestDto(999L, 1L, "테스트 메시지");
        
        when(optionRepository.findById(999L)).thenReturn(Optional.empty());
        
        // when & then
        assertThrows(NoOptionInfoException.class, () ->
            orderService.orderProduct(requestDto, member));
    }
}