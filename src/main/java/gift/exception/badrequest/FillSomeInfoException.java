package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class FillSomeInfoException extends BadRequestException {
    public FillSomeInfoException() {
        super("한 가지 이상의 정보는 있어야 수정이 가능합니다.");
    }
}
