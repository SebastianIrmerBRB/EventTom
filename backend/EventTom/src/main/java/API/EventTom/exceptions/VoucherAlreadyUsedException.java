package API.EventTom.exceptions;

public class VoucherAlreadyUsedException extends RuntimeException {
    public VoucherAlreadyUsedException(String message) {
        super(message);
    }
}

