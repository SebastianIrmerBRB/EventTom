package API.EventTom.exceptions;

public class UnauthorizedVoucherUseException extends RuntimeException {
    public UnauthorizedVoucherUseException(String message) {
        super(message);
    }
}
