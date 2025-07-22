package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class WrongProductCntException extends BadRequestException {
    public WrongProductCntException() {
        super("상품 수량은 0보다 커야 합니다.");
    }
}
