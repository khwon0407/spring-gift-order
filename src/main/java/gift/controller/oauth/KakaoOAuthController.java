package gift.controller.oauth;

import gift.config.KakaoProperties;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class KakaoOAuthController {
    
    private final KakaoProperties properties;
    
    public KakaoOAuthController(KakaoProperties kakaoProperties) {
        this.properties = kakaoProperties;
    }
    
    @GetMapping
    public ResponseEntity<String> getAccessToken(
        @RequestParam(name = "code") String authorizationCode
    ) {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", authorizationCode);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(request, KakaoTokenResponseDto.class);
        
        KakaoTokenResponseDto tokenResponse = response.getBody();
        
        String accessToken = tokenResponse.getAccessToken();
        
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }
}
