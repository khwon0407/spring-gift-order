package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class WrongProductOptionException extends BadRequestException {
    public WrongProductOptionException() {
        super("요청하신 상품에 없는 옵션입니다.");
    }
}
