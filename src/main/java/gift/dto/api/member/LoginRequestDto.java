package gift.dto.api.member;

import gift.entity.Member;
import gift.entity.Role;

public record LoginRequestDto(Long id, String email, String password, Role role) {
    public LoginRequestDto(Member member) {
        this(member.getId(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
