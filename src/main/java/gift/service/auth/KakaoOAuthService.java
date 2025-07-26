package gift.service.auth;

import gift.dto.api.member.MemberResponseDto;

public interface KakaoOAuthService {
    
    String getKakaoLoginLink();
    
    MemberResponseDto kakaoLogin(String authorizationCode);
}
