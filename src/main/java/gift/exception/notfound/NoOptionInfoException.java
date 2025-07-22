package gift.exception.notfound;

import gift.exception.common.NotFoundException;

public class NoOptionInfoException extends NotFoundException {
    public NoOptionInfoException() {
        super("요청하신 정보에 해당하는 옵션 정보가 없습니다.");
    }
}
