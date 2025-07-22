package gift.exception.unauthorized;

import gift.exception.common.UnauthorizedException;

public class WrongHeaderException extends UnauthorizedException {
    public WrongHeaderException() {
        super("유효하지 않은 Authorization 헤더입니다.");
    }
}
