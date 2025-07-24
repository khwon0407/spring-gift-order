package gift.service.auth;

import gift.dto.api.oauth.KakaoLoginResponseDto;

public interface KakaoOAuthService {
    
    String getKakaoLoginLink();
    
    KakaoLoginResponseDto kakaoLogin(String authorizationCode);
}
