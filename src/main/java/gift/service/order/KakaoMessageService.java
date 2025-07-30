package gift.service.order;

import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;

public interface KakaoMessageService {
    
    void sendKakaoMessage(Member user, OrderResponseDto responseDto);
}
