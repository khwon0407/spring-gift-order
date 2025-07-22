package gift.exception.badrequest;

import gift.exception.common.BadRequestException;

public class WrongCriteriaException extends BadRequestException {
    public WrongCriteriaException() {
        super("잘못된 정렬 기준입니다.");
    }
}
