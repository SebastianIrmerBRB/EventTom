package API.EventTom.exceptions.RuntimeExceptions;

public class ExpiredVoucherException extends RuntimeException {
    public ExpiredVoucherException(String message) {
        super(message);
    }
}