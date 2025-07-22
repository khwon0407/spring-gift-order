package gift.exception.conflict;

import gift.exception.common.ConflictException;

public class AlreadyRegisteredException extends ConflictException {
    public AlreadyRegisteredException() {
        super("이미 등록된 이메일입니다.");
    }
}
