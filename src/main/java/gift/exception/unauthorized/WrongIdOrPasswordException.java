package gift.exception.unauthorized;

import gift.exception.common.UnauthorizedException;

public class WrongIdOrPasswordException extends UnauthorizedException {
    public WrongIdOrPasswordException() {
        super("잘못된 ID 혹은 비밀번호입니다.");
    }
}
