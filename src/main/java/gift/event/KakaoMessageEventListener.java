package gift.event;

import gift.service.order.KakaoMessageService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class KakaoMessageEventListener {

    private final KakaoMessageService kakaoMessageService;
    
    public KakaoMessageEventListener(KakaoMessageService kakaoMessageService) {
        this.kakaoMessageService = kakaoMessageService;
    }
    
    @Async
    @EventListener
    public void handleOrderCompleted(OrderCompletedEvent event) {
        kakaoMessageService.sendKakaoMessage(event.getUser(), event.getResponseDto());
    }
}