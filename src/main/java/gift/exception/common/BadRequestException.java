package gift.exception.common;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
