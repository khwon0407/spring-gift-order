package gift.dto.api.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record MemberRequestDto(
    @Email(message = "이메일 형식이 잘못되었습니다.")
    @NotNull(message = "이메일은 공란일 수 없습니다.")
    String email,
    @NotNull(message = "비밀번호는 공란일 수 없습니다.")
    String password) {
    
}
