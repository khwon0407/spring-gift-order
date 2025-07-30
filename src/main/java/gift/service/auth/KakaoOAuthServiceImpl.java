package gift.service.auth;

import gift.auth.JwtProvider;
import gift.dto.api.member.MemberResponseDto;
import gift.dto.api.oauth.KakaoTokenResponseDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.external.KakaoClient;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuthServiceImpl implements KakaoOAuthService {
    
    private final KakaoClient kakaoClient;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    
    public KakaoOAuthServiceImpl(KakaoClient kakaoClient,
        JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.kakaoClient = kakaoClient;
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }
    
    @Override
    public String getKakaoLoginLink() {
        return kakaoClient.getKakaoLoginLink();
    }
    
    @Override
    public MemberResponseDto kakaoLogin(String authorizationCode) {
        
        KakaoTokenResponseDto tokenResponse = kakaoClient.getKakaoToken(authorizationCode);
        String accessToken = tokenResponse.getAccessToken();
        
        String email = kakaoClient.getKakaoEmail(accessToken);
        
        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(
                new Member(null, email, "kakaopw", Role.USER, accessToken)));
        
        String authToken = jwtProvider.createToken(member);
        
        return new MemberResponseDto(authToken);
    }
}
