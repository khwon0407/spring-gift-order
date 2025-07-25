package gift.service.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.auth.JwtProvider;
import gift.config.KakaoProperties;
import gift.dto.api.oauth.KakaoLoginResponseDto;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import gift.dto.api.oauth.KakaoUserResponseDto;
import gift.dto.api.oauth.KakaoUserResponseDto.KakaoAccount;
import gift.entity.Member;
import gift.entity.Role;
import gift.repository.member.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class KakaoOAuthServiceImplTest {
    
    @Mock
    private KakaoProperties properties;
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private JwtProvider jwtProvider;
    
    @Mock
    private MemberRepository memberRepository;
    
    @InjectMocks
    private KakaoOAuthServiceImpl kakaoOAuthService;
    
    @Test
    void 카카오_엑세스토큰_획득을_위한_링크를_얻어온다() {
        // given
        when(properties.clientId()).thenReturn("test-client-id");
        when(properties.redirectUri()).thenReturn("http://localhost:8080");
        
        // when
        String loginLink = kakaoOAuthService.getKakaoLoginLink();
        
        // then
        assertThat(loginLink)
            .contains("https://kauth.kakao.com/oauth/authorize")
            .contains("client_id=test-client-id")
            .contains("redirect_uri=http://localhost:8080");
    }
    
    @Test
    void 신규멤버로_로그인한다() {
        // given
        when(properties.clientId()).thenReturn("test-client-id");
        when(properties.redirectUri()).thenReturn("http://localhost:8080");
        
        String authorizationCode = "test-code";
        String kakaoAccessToken = "kakao-access-token";
        String email = "test@kakao.com";
        String jwtToken = "jwt-token";
        
        KakaoTokenResponseDto tokenResponse = new KakaoTokenResponseDto();
        tokenResponse.setAccessToken(kakaoAccessToken);
        
        KakaoUserResponseDto userResponse = new KakaoUserResponseDto(
            new KakaoAccount(email)
        );
        
        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoTokenResponseDto.class)))
            .thenReturn(ResponseEntity.ok(tokenResponse));
        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoUserResponseDto.class)))
            .thenReturn(ResponseEntity.ok(userResponse));
        
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        when(jwtProvider.createToken(any(Member.class))).thenReturn(jwtToken);
        
        // when
        KakaoLoginResponseDto response = kakaoOAuthService.kakaoLogin(authorizationCode);
        
        // then
        assertThat(response.accessToken()).isEqualTo(kakaoAccessToken);
        assertThat(response.authToken()).isEqualTo(jwtToken);
        
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        assertThat(memberCaptor.getValue().getEmail()).isEqualTo(email);
    }
    
    @Test
    void 기존멤버로_로그인한다() {
        // given
        when(properties.clientId()).thenReturn("test-client-id");
        when(properties.redirectUri()).thenReturn("http://localhost:8080");
        
        String authorizationCode = "test-code";
        String kakaoAccessToken = "kakao-access-token";
        String email = "existing@kakao.com";
        String jwtToken = "jwt-token";
        
        KakaoTokenResponseDto tokenResponse = new KakaoTokenResponseDto();
        tokenResponse.setAccessToken(kakaoAccessToken);
        
        KakaoUserResponseDto userResponse = new KakaoUserResponseDto(
            new KakaoAccount(email)
        );
        
        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoTokenResponseDto.class)))
            .thenReturn(ResponseEntity.ok(tokenResponse));
        when(restTemplate.exchange(any(RequestEntity.class), eq(KakaoUserResponseDto.class)))
            .thenReturn(ResponseEntity.ok(userResponse));
        
        Member existingMember = new Member(1L, email, "kakaopw", Role.USER);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(existingMember));
        when(jwtProvider.createToken(existingMember)).thenReturn(jwtToken);
        
        // when
        KakaoLoginResponseDto response = kakaoOAuthService.kakaoLogin(authorizationCode);
        
        // then
        assertThat(response.accessToken()).isEqualTo(kakaoAccessToken);
        assertThat(response.authToken()).isEqualTo(jwtToken);
        verify(memberRepository, never()).save(any(Member.class));
    }
    
}