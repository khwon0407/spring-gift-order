package gift.exception.notfound;

import gift.exception.common.NotFoundException;

public class NotInWishlistException extends NotFoundException {
    public NotInWishlistException() {
        super("위시리스트에 등록되지 않은 정보입니다.");
    }
}
