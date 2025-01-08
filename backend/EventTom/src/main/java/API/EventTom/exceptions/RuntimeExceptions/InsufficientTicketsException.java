package API.EventTom.exceptions.RuntimeExceptions;

public class InsufficientTicketsException extends RuntimeException {
    public InsufficientTicketsException(String message) {
        super(message);
    }
}