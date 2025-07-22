package gift.exception.forbidden;

import gift.exception.common.ForbiddenException;

public class WrongPermissionException extends ForbiddenException {
    public WrongPermissionException() {
        super("해당 기능을 실행할 권한이 없습니다.");
    }
    
}
