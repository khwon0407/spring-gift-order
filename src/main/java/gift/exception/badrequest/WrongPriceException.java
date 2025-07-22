package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class WrongPriceException extends BadRequestException {
    public WrongPriceException() {
        super("가격은 0원 이상이어야 합니다.");
    }
}
