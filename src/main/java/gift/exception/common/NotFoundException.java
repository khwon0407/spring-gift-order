package gift.exception.common;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
