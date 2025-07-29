package gift.service.auth;

import gift.auth.JwtProvider;
import gift.config.KakaoProperties;
import gift.dto.api.member.MemberResponseDto;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import gift.dto.api.oauth.KakaoUserResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.repository.member.MemberRepository;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoOAuthServiceImpl implements KakaoOAuthService {
    
    private final KakaoProperties properties;
    private final RestClient restClient;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    
    public KakaoOAuthServiceImpl(KakaoProperties properties, RestClient.Builder builder,
        JwtProvider jwtProvider, MemberRepository memberRepository) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(1));
        requestFactory.setReadTimeout(Duration.ofSeconds(2));
        
        this.properties = properties;
        this.restClient = builder.requestFactory(requestFactory).build();
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }
    
    @Override
    public String getKakaoLoginLink() {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId())
            .queryParam("redirect_uri", properties.redirectUri())
            .build()
            .toUriString();
    }
    
    @Override
    public MemberResponseDto kakaoLogin(String authorizationCode) {
        
        KakaoTokenResponseDto tokenResponse = getKakaoToken(authorizationCode);
        String accessToken = tokenResponse.getAccessToken();
        
        String email = getKakaoEmail(accessToken);
        
        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(
                new Member(null, email, "kakaopw", Role.USER, accessToken)));
        
        String authToken = jwtProvider.createToken(member);
        
        return new MemberResponseDto(authToken);
    }
    
    private KakaoTokenResponseDto getKakaoToken(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", authorizationCode);
        
        return restClient.post()
            .uri(url)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(body)
            .retrieve()
            .body(KakaoTokenResponseDto.class);
    }
    
    private String getKakaoEmail(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        
        KakaoUserResponseDto response = restClient.get()
            .uri(url)
            .headers(headers -> headers.setBearerAuth(accessToken))
            .retrieve()
            .body(KakaoUserResponseDto.class);
        
        return response.getKakaoAccount().getEmail();
    }
}
