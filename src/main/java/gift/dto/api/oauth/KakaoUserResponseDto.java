package gift.dto.api.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserResponseDto {
    
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
    
    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }
    
    public static class KakaoAccount {
        private String email;
        public String getEmail() { return email; }
    }
}
