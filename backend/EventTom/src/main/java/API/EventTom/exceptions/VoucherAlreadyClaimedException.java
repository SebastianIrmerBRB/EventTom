package API.EventTom.exceptions;

public class VoucherAlreadyClaimedException extends RuntimeException {
    public VoucherAlreadyClaimedException(String message) {
        super(message);
    }
}
