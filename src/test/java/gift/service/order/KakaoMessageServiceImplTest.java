package gift.service.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class KakaoMessageServiceImplTest {
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private KakaoMessageServiceImpl kakaoMessageService;
    
    @Test
    void sendKakaoMessage_success() {
        // given
        Member user = new Member(1L, "test@test.com", "pwpw", Role.USER, "testAccessToken");
        
        OrderResponseDto responseDto = new OrderResponseDto(1L, 1L, 2L, LocalDateTime.now(), "테스트 메시지");
        
        when(restTemplate.exchange(any(RequestEntity.class), eq(String.class)))
            .thenReturn(ResponseEntity.ok("OK"));
        
        // when
        kakaoMessageService.sendKakaoMessage(user, responseDto);
        
        // then
        verify(restTemplate, times(1))
            .exchange(any(RequestEntity.class), eq(String.class));
    }
    
    @Test
    void sendKakaoMessage_skipWhenNoAccessToken() {
        // given
        Member user = new Member(1L, "test@test.com", "pwpw", Role.USER);
        
        OrderResponseDto responseDto = new OrderResponseDto(1L, 1L, 2L, LocalDateTime.now(), "테스트 메시지");
        
        // when
        kakaoMessageService.sendKakaoMessage(user, responseDto);
        
        // then
        verify(restTemplate, never())
            .exchange(any(RequestEntity.class), eq(String.class));
    }
}