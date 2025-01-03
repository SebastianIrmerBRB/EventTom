package API.EventTom.exceptions;

public class VoucherExpiredException extends RuntimeException {
    public VoucherExpiredException(String message) {
        super(message);
    }
}