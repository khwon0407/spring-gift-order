package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class CheckMdOkException extends BadRequestException {
    public CheckMdOkException() {
        super("MD와의 협의 후 사용 가능한 이름입니다.");
    }
}
