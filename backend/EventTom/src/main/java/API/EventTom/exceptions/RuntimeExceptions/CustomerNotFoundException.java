package API.EventTom.exceptions.RuntimeExceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String customerNumber) {
        super("Customer not found with number: " + customerNumber);
    }
}