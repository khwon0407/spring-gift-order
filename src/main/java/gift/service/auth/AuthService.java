package gift.service.auth;

import gift.dto.api.member.LoginRequestDto;
import gift.dto.api.member.MemberResponseDto;

public interface AuthService {
    MemberResponseDto login(LoginRequestDto requestDto);
}
