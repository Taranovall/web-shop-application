package web.shop.exception.not.found;

import web.shop.exception.DataAccessException;

public class NotFoundException extends DataAccessException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
