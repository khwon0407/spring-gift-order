package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class LessQuantityException extends BadRequestException {
    public LessQuantityException() {
        super("재고가 부족합니다.");
    }
}
