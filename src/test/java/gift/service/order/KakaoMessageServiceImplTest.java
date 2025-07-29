package gift.service.order;

import static org.mockito.Mockito.*;

import gift.dto.api.order.OrderResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.external.KakaoClient;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoMessageServiceImplTest {
    @Mock
    private KakaoClient kakaoClient;
    
    @InjectMocks
    private KakaoMessageServiceImpl kakaoMessageService;
    
    @Test
    void 카카오메시지_성공() {
        // given
        Member user = new Member(1L, "test@test.com", "pwpw", Role.USER, "testAccessToken");
        OrderResponseDto responseDto = new OrderResponseDto(1L, 1L, 2L, LocalDateTime.now(), "테스트 메시지");
        
        // when
        kakaoMessageService.sendKakaoMessage(user, responseDto);
        
        // then
        verify(kakaoClient, times(1)).sendKakaoMessage(user, responseDto);
    }
}