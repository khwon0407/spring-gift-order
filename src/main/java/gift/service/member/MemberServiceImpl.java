package gift.service.member;

import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberRequestDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.conflict.AlreadyRegisteredException;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    @Override
    @Transactional
    public LoginRequestDto registerMember(MemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new AlreadyRegisteredException();
        }
        
        Member newMember = new Member(null, requestDto.email(), requestDto.password(), Role.USER);
        
        Member registeredMember = memberRepository.save(newMember);
        
        return new LoginRequestDto(registeredMember);
    }
    
    @Override
    public LoginRequestDto findMemberToLogin(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email()).orElseThrow(
            WrongIdOrPasswordException::new);
        return new LoginRequestDto(member);
    }
}
