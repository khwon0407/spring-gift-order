package gift.external;

import gift.dto.api.oauth.KakaoTokenResponseDto;
import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;

public interface KakaoClient {
    String getKakaoLoginLink();
    KakaoTokenResponseDto getKakaoToken(String authorizationCode);
    String getKakaoEmail(String accessToken);
    
    void sendKakaoMessage(Member user, OrderResponseDto responseDto);
}
