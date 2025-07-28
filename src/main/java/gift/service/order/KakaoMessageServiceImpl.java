package gift.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.api.order.OrderResponseDto;
import gift.dto.api.order.TextTemplate;
import gift.entity.Member;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class KakaoMessageServiceImpl implements KakaoMessageService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(KakaoMessageServiceImpl.class);
    
    public KakaoMessageServiceImpl(RestClient.Builder builder, ObjectMapper objectMapper) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        
        this.restClient = builder
            .requestFactory(requestFactory)
            .build();
        
        this.objectMapper = objectMapper;
    }
    
    @Override
    @Async
    public void sendKakaoMessage(Member user, OrderResponseDto responseDto) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        
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
