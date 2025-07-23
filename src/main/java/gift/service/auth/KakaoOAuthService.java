package gift.service.auth;

import gift.dto.api.oauth.KakaoAccessTokenResponseDto;

public interface KakaoOAuthService {
    
    KakaoAccessTokenResponseDto getKakaoAccessToken(String authorizationCode);
}
