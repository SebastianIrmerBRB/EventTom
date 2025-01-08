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
import API.EventTom.services.tickets.interfaces.ITicketCreationService;
import API.EventTom.services.tickets.interfaces.ITicketPurchaseService;
import API.EventTom.services.vouchers.interfaces.IVoucherUsageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class TicketCreationServiceImpl implements ITicketCreationService {
    private final TicketRepository ticketRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    public PurchaseResult processTicketPurchase(
            Event event, Customer customer, int amount,
            BigDecimal baseTicketPrice, BigDecimal totalVoucherDiscount) {

        List<Long> ticketIds = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal remainingDiscount = totalVoucherDiscount;

        for (int i = 0; i < amount; i++) {
            TicketPriceResult priceResult = calculateTicketPriceWithDiscount(
                    baseTicketPrice, remainingDiscount);

            Ticket ticket = createAndSaveTicket(event, customer,
                    baseTicketPrice, priceResult.getFinalPrice());

            ticketIds.add(ticket.getId());
            totalPrice = totalPrice.add(priceResult.getFinalPrice());
            remainingDiscount = priceResult.getRemainingDiscount();

        }
        return new PurchaseResult(ticketIds, totalPrice);
    }

    private TicketPriceResult calculateTicketPriceWithDiscount(
            BigDecimal basePrice, BigDecimal remainingDiscount) {

        if (remainingDiscount.compareTo(BigDecimal.ZERO) <= 0) {
            return new TicketPriceResult(basePrice, BigDecimal.ZERO);
        }

        BigDecimal finalPrice = basePrice.subtract(remainingDiscount)
                .max(BigDecimal.ZERO)
                .setScale(SCALE, ROUNDING_MODE);

        BigDecimal newRemainingDiscount = finalPrice.compareTo(BigDecimal.ZERO) == 0
                ? remainingDiscount.subtract(basePrice)
                : BigDecimal.ZERO;

        return new TicketPriceResult(finalPrice, newRemainingDiscount);
    }

    private Ticket createAndSaveTicket(Event event, Customer customer,
                                       BigDecimal basePrice, BigDecimal finalPrice) {
        Ticket ticket = createTicket(event, customer, basePrice, finalPrice);
        ticket = ticketRepository.save(ticket);
        publishTicketPurchaseEvent(ticket, event);
        return ticket;
    }

    private Ticket createTicket(Event event, Customer customer, BigDecimal basePrice, BigDecimal finalPrice) {
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setStatusUsed(false);
        ticket.setBasePrice(basePrice);
        ticket.setFinalPrice(finalPrice);
        return ticket;
    }

    private void publishTicketPurchaseEvent(Ticket ticket, Event event) {
        eventPublisher.publishEvent(new TicketPurchaseEvent(this, ticket, event));
    }
}