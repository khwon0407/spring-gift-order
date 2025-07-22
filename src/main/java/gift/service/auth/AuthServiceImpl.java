package gift.service.auth;

import gift.auth.JwtProvider;
import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.entity.Member;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    
    public AuthServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    public MemberResponseDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(WrongIdOrPasswordException::new);
        
        if(!member.getPassword().equals(requestDto.password())) {
            throw new WrongIdOrPasswordException();
        }
        
        String accessToken = jwtProvider.createToken(member);
        
        return new MemberResponseDto(accessToken);
    }
}
