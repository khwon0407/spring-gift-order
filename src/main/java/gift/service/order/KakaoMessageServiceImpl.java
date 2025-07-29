package gift.service.order;

import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;
import gift.external.KakaoClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KakaoMessageServiceImpl implements KakaoMessageService {
    private final KakaoClient kakaoClient;
    
    public KakaoMessageServiceImpl(KakaoClient kakaoClient) {
        this.kakaoClient = kakaoClient;
    }
    
    @Override
    @Async
    public void sendKakaoMessage(Member user, OrderResponseDto responseDto) {
        kakaoClient.sendKakaoMessage(user, responseDto);
    }
}
