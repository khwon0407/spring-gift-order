package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class WrongOptionQuantityException extends BadRequestException {
    public WrongOptionQuantityException() {
        super("주문할 수 없는 수량입니다.");
    }
}
