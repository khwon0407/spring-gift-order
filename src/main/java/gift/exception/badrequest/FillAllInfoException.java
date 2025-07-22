package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class FillAllInfoException extends BadRequestException {
    public FillAllInfoException() {
        super("모든 정보가 있어야 수정이 가능합니다.");
    }
}
