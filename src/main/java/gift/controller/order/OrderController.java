package gift.controller.order;

import gift.config.annotation.CurrentUser;
import gift.config.annotation.ValidHeader;
import gift.dto.api.order.OrderRequestDto;
import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;
import gift.service.order.KakaoMessageService;
import gift.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final KakaoMessageService kakaoMessageService;
    
    public OrderController(OrderService orderService, KakaoMessageService kakaoMessageService) {
        this.orderService = orderService;
        this.kakaoMessageService = kakaoMessageService;
    }
    
    @PostMapping
    @ValidHeader
    public ResponseEntity<OrderResponseDto> orderProduct(
        @CurrentUser Member user,
        @RequestBody OrderRequestDto requestDto
    ) {
        OrderResponseDto responseDto = orderService.orderProduct(requestDto, user);
        kakaoMessageService.sendKakaoMessage(user, responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
