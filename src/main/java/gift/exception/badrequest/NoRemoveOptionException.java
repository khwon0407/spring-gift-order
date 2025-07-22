package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class NoRemoveOptionException extends BadRequestException {
    public NoRemoveOptionException() {
        super("옵션은 최소 1개 이상 있어야 합니다.");
    }
}
