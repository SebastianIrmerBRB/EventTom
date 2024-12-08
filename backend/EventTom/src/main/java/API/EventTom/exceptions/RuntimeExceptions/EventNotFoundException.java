package API.EventTom.exceptions.RuntimeExceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long eventId) {
        super("Event not found with ID: " + eventId);
    }
}
