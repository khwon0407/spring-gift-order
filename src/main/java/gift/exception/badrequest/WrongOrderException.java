package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class WrongOrderException extends BadRequestException {
    public WrongOrderException() {
        super("지원하지 않는 정렬 방식입니다.");
    }
}
