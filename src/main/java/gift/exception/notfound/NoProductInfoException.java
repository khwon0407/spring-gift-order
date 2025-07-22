package gift.exception.notfound;

import gift.exception.common.NotFoundException;

public class NoProductInfoException extends NotFoundException {
    public NoProductInfoException() {
        super("요청하신 정보에 해당하는 상품 정보가 없습니다.");
    }
}
