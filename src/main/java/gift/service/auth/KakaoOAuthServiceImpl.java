package gift.service.auth;

import gift.config.KakaoProperties;
import gift.dto.api.oauth.KakaoAccessTokenResponseDto;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOAuthServiceImpl implements KakaoOAuthService {
    
    private final KakaoProperties properties;
    
    public KakaoOAuthServiceImpl(KakaoProperties properties) {
        this.properties = properties;
    }
    
    @Override
    public KakaoAccessTokenResponseDto getKakaoAccessToken(String authorizationCode) {
        
        RequestEntity<MultiValueMap<String, String>> request = createRequest(authorizationCode);
        KakaoTokenResponseDto tokenResponse = getKakaoToken(request);
        
        return new KakaoAccessTokenResponseDto(tokenResponse.getAccessToken());
    }
    
    private KakaoTokenResponseDto getKakaoToken(RequestEntity<MultiValueMap<String, String>> request) {
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(request, KakaoTokenResponseDto.class);
        
        return response.getBody();
    }
    
    private RequestEntity<MultiValueMap<String, String>> createRequest(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", authorizationCode);
        
        return new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
    }
}
