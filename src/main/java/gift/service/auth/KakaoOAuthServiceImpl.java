package gift.service.auth;

import gift.auth.JwtProvider;
import gift.config.KakaoProperties;
import gift.dto.api.oauth.KakaoLoginResponseDto;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import gift.dto.api.oauth.KakaoUserResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.repository.member.MemberRepository;
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
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoOAuthServiceImpl implements KakaoOAuthService {
    
    private final KakaoProperties properties;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    
    public KakaoOAuthServiceImpl(KakaoProperties properties, RestTemplate restTemplate,
        JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.properties = properties;
        this.restTemplate = restTemplate;
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
    public KakaoLoginResponseDto kakaoLogin(String authorizationCode) {
        
        RequestEntity<MultiValueMap<String, String>> request = createRequest(authorizationCode);
        KakaoTokenResponseDto tokenResponse = getKakaoToken(request);
        String accessToken = tokenResponse.getAccessToken();
        
        String email = getKakaoEmail(accessToken);
        
        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(new Member(null, email, "kakaopw", Role.USER)));
        
        String authToken = jwtProvider.createToken(member);
        
        return new KakaoLoginResponseDto(accessToken, authToken);
    }
    
    private RequestEntity<MultiValueMap<String, String>> createRequest(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", authorizationCode);
        
        return new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
    }
    
    private KakaoTokenResponseDto getKakaoToken(RequestEntity<MultiValueMap<String, String>> request) {
        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(request, KakaoTokenResponseDto.class);
        
        return response.getBody();
    }
    
    private String getKakaoEmail(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        
        ResponseEntity<KakaoUserResponseDto> response = restTemplate.exchange(
            request,
            KakaoUserResponseDto.class
        );
        
        return response.getBody().getKakaoAccount().getEmail();
    }
}
