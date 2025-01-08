package API.EventTom.services.tickets;
import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.DTO.response.TicketPurchaseResponseDTO;
import API.EventTom.exceptions.EventDatePassedException;
import API.EventTom.exceptions.InvalidPurchaseAmountException;
import API.EventTom.exceptions.RuntimeExceptions.CustomerNotFoundException;
import API.EventTom.exceptions.RuntimeExceptions.EventNotFoundException;
import API.EventTom.exceptions.RuntimeExceptions.InsufficientTicketsException;
import API.EventTom.models.Customer;
import API.EventTom.models.Event;
import API.EventTom.models.Ticket;
import API.EventTom.models.Voucher;
import API.EventTom.observers.TicketPurchaseEvent;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.repositories.EventRepository;
import API.EventTom.repositories.TicketRepository;
import API.EventTom.services.notifications.WebSocketNotificationService;
import API.EventTom.services.tickets.interfaces.ITicketPurchaseService;
import API.EventTom.services.tickets.interfaces.ITicketValidator;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class TicketValidatorImpl implements ITicketValidator {
    private static final int MAX_TICKETS_PER_PURCHASE = 10;

    @Override
    public void validatePurchaseRequest(Event event, PurchaseTicketDTO dto) {
        validateTicketAvailability(event, dto.getAmount());
        validateEventDate(event);
        validatePurchaseAmount(dto.getAmount());
    }

    @Override
    public void validateTicketAvailability(Event event, int requestedAmount) {
        if (event.getAvailableTickets() < requestedAmount) {
            throw new InsufficientTicketsException(
                    String.format("Not enough tickets available. Requested: %d, Available: %d",
                            requestedAmount, event.getAvailableTickets())
            );
        }
    }

    @Override
    public void validateEventDate(Event event) {
        if (event.getDateOfEvent().isBefore(LocalDateTime.now())) {
            throw new EventDatePassedException("Cannot purchase tickets for past events");
        }
    }

    @Override
    public void validatePurchaseAmount(int amount) {
        if (amount <= 0) {
            throw new InvalidPurchaseAmountException("Purchase amount must be greater than zero");
        }
        if (amount > MAX_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseAmountException(
                    String.format("Cannot purchase more than %d tickets at once", MAX_TICKETS_PER_PURCHASE)
            );
        }
    }
}