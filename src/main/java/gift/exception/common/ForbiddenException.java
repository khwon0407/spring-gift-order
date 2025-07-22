package gift.exception.common;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {
    public ForbiddenException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
