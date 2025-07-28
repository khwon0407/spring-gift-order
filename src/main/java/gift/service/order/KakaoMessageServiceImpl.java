package gift.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.api.order.OrderResponseDto;
import gift.dto.api.order.TextTemplate;
import gift.entity.Member;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMessageServiceImpl implements KakaoMessageService {
    private final RestTemplate restTemplate;
    
    public KakaoMessageServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    @Async
    public void sendKakaoMessage(Member user, OrderResponseDto responseDto) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        
        String accessToken = user.getAccessToken();
        if(accessToken == null) {
            return;
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);
        
        String templateObject = createTextTemplate(responseDto.getMessage());
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);
        
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(
            body, headers, HttpMethod.POST, URI.create(url)
        );
        
        restTemplate.exchange(request, String.class);
    }
    
    private String createTextTemplate(String message) {
        TextTemplate template = new TextTemplate(message);
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            return objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }
}
