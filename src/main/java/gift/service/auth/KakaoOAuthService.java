package gift.service.auth;

import gift.dto.api.oauth.KakaoAccessTokenResponseDto;

public interface KakaoOAuthService {
    
    String getKakaoLoginLink();
    
    KakaoAccessTokenResponseDto getKakaoAccessToken(String authorizationCode);
}
