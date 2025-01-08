package API.EventTom.services.tickets.interfaces;

import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.models.Customer;
import API.EventTom.models.Event;

public interface ITicketValidator {
    void validatePurchaseRequest(Event event, PurchaseTicketDTO dto);
    void validateTicketAvailability(Event event, int requestedAmount);
    void validateEventDate(Event event);
    void validatePurchaseAmount(int amount);
}