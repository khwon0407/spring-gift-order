package gift.service.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.auth.JwtProvider;
import gift.dto.api.member.MemberResponseDto;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.external.KakaoClient;
import gift.repository.member.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoOAuthServiceImplTest {
    
    @Mock
    private KakaoClient kakaoClient;
    
    @Mock
    private JwtProvider jwtProvider;
    
    @Mock
    private MemberRepository memberRepository;
    
    @InjectMocks
    private KakaoOAuthServiceImpl kakaoOAuthService;
    
    @Test
    void 카카오_엑세스토큰_획득을_위한_링크를_얻어온다() {
        // given
        String expectedLink = "https://kauth.kakao.com/oauth/authorize?client_id=test-client-id";
        when(kakaoClient.getKakaoLoginLink()).thenReturn(expectedLink);
        
        // when
        String loginLink = kakaoOAuthService.getKakaoLoginLink();
        
        // then
        assertThat(loginLink).isEqualTo(expectedLink);
    }
    
    @Test
    void 신규멤버로_로그인한다() {
        // given
        String authorizationCode = "test-code";
        String kakaoAccessToken = "kakao-access-token";
        String email = "test@kakao.com";
        String jwtToken = "jwt-token";
        
        KakaoTokenResponseDto tokenResponse = new KakaoTokenResponseDto();
        tokenResponse.setAccessToken(kakaoAccessToken);
        
        when(kakaoClient.getKakaoToken(authorizationCode)).thenReturn(tokenResponse);
        when(kakaoClient.getKakaoEmail(kakaoAccessToken)).thenReturn(email);
        
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        when(jwtProvider.createToken(any(Member.class))).thenReturn(jwtToken);
        
        // when
        MemberResponseDto response = kakaoOAuthService.kakaoLogin(authorizationCode);
        
        // then
        assertThat(response.token()).isEqualTo(jwtToken);
        
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        assertThat(memberCaptor.getValue().getEmail()).isEqualTo(email);
    }
    
    @Test
    void 기존멤버로_로그인한다() {
        // given
        String authorizationCode = "test-code";
        String kakaoAccessToken = "kakao-access-token";
        String email = "existing@kakao.com";
        String jwtToken = "jwt-token";
        
        KakaoTokenResponseDto tokenResponse = new KakaoTokenResponseDto();
        tokenResponse.setAccessToken(kakaoAccessToken);
        
        when(kakaoClient.getKakaoToken(authorizationCode)).thenReturn(tokenResponse);
        when(kakaoClient.getKakaoEmail(kakaoAccessToken)).thenReturn(email);
        
        Member existingMember = new Member(1L, email, "kakaopw", Role.USER);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(existingMember));
        when(jwtProvider.createToken(existingMember)).thenReturn(jwtToken);
        
        // when
        MemberResponseDto response = kakaoOAuthService.kakaoLogin(authorizationCode);
        
        // then
        assertThat(response.token()).isEqualTo(jwtToken);
        verify(memberRepository, never()).save(any(Member.class));
    }
}