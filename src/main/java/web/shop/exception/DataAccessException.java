package web.shop.exception;

public class DataAccessException extends Exception {

    public DataAccessException(String message, Exception cause) {
        super(message, cause);
    }

    public DataAccessException(String message) {
        super(message);
    }
}
