package API.EventTom.services.tickets.interfaces;

import API.EventTom.models.Customer;
import API.EventTom.models.Event;

public interface ITicketValidator {
    void validatePurchase(Event event, Customer customer);
}
