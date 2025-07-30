package gift.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import gift.dto.api.oauth.KakaoUserResponseDto;
import gift.dto.api.order.OrderResponseDto;
import gift.dto.api.order.TextTemplate;
import gift.entity.Member;
import gift.service.order.KakaoMessageServiceImpl;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoClientImpl implements KakaoClient {
    private final RestClient restClient;
    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(KakaoMessageServiceImpl.class);
    
    public KakaoClientImpl(RestClient.Builder builder, KakaoProperties properties, ObjectMapper objectMapper) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(1));
        requestFactory.setReadTimeout(Duration.ofSeconds(2));
        
        this.properties = properties;
        this.restClient = builder.requestFactory(requestFactory).build();
        this.objectMapper = objectMapper;
    }
    
    public String getKakaoLoginLink() {
        return UriComponentsBuilder.fromUriString(properties.authorizeUrl())
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId())
            .queryParam("redirect_uri", properties.redirectUri())
            .build()
            .toUriString();
    }
    
    @Override
    public KakaoTokenResponseDto getKakaoToken(String authorizationCode) {
        String url = properties.tokenUrl();
        
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
    
    @Override
    public String getKakaoEmail(String accessToken) {
        String url = properties.userInfoUrl();
        
        KakaoUserResponseDto response = restClient.get()
            .uri(url)
            .headers(headers -> headers.setBearerAuth(accessToken))
            .retrieve()
            .body(KakaoUserResponseDto.class);
        
        return response.getKakaoAccount().getEmail();
    }
    
    @Override
    public void sendKakaoMessage(Member user, OrderResponseDto responseDto) {
        String url = properties.messageSendUrl();
        
        String accessToken = user.getAccessToken();
        if (accessToken == null) {
            return;
        }
        
        String templateObject = createTextTemplate(responseDto.getMessage());
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);
        
        try {
            restClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            log.error("카카오 메시지 전송 실패: {}", e.getMessage(), e);
        }
    }
    
    private String createTextTemplate(String message) {
        TextTemplate template = new TextTemplate(message);
        
        try {
            return objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }
}
