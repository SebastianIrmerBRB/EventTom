package API.EventTom.exceptions;

public class InvalidPurchaseAmountException extends RuntimeException {
    public InvalidPurchaseAmountException(String message) {
        super(message);
    }
}