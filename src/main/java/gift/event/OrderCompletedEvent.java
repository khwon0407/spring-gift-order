package gift.event;

import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;

public class OrderCompletedEvent {
    private final Member user;
    private final OrderResponseDto responseDto;

    public OrderCompletedEvent(Member user, OrderResponseDto responseDto) {
        this.user = user;
        this.responseDto = responseDto;
    }

    public Member getUser() {
        return user;
    }

    public OrderResponseDto getResponseDto() {
        return responseDto;
    }
}