package gift.service.order;

import gift.dto.api.order.OrderRequestDto;
import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;

public interface OrderService {
    
    OrderResponseDto orderProduct(OrderRequestDto requestDto, Member user);
}
