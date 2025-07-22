package gift.exception.common;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {
    public ConflictException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
