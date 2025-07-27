package gift.controller.order;

import gift.config.annotation.CurrentUser;
import gift.config.annotation.ValidHeader;
import gift.entity.Member;
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
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    @ValidHeader
    public ResponseEntity<OrderResponseDto> orderProduct(
        @RequestBody OrderRequestDto requestDto,
        @CurrentUser Member user
    ) {
        OrderResponseDto responseDto = orderService.orderProduct(requestDto, user);
        return new ResponseEntity<OrderResponseDto>(responseDto, HttpStatus.CREATED);
    }
}
