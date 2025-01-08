package API.EventTom.services.tickets;

import API.EventTom.DTO.request.PurchaseTicketDTO;
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
import API.EventTom.services.tickets.interfaces.ITicketPurchaseService;
import API.EventTom.services.vouchers.interfaces.IVoucherUsageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class TicketPurchaseServiceImpl implements ITicketPurchaseService {
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final IVoucherUsageService voucherUsageService;

    @Override
    @Transactional
    public void purchaseTicket(PurchaseTicketDTO purchaseTicketDTO) {
        Event event = eventRepository.findById(purchaseTicketDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(purchaseTicketDTO.getEventId()));

        // Check if enough tickets are available
        validateTicketAvailability(event, purchaseTicketDTO.getAmount());

        Customer customer = customerRepository.findCustomerByCustomerNumber(purchaseTicketDTO.getCustomerNumber())
                .orElseThrow(() -> new CustomerNotFoundException(purchaseTicketDTO.getCustomerNumber()));

        BigDecimal currentPrice = calculateTicketPrice(event);
        BigDecimal totalPrice = currentPrice.multiply(BigDecimal.valueOf(purchaseTicketDTO.getAmount()));

        BigDecimal finalPrice = totalPrice;
        if (purchaseTicketDTO.getVoucherCode() != null && !purchaseTicketDTO.getVoucherCode().isEmpty()) {
            Voucher voucher = voucherUsageService.validateVoucher(purchaseTicketDTO.getVoucherCode());
            finalPrice = voucherUsageService.calculateDiscountedAmount(totalPrice, voucher);
            voucherUsageService.useVoucherForPurchase(
                    purchaseTicketDTO.getVoucherCode(),
                    customer.getId(),
                    totalPrice
            );
        }

        for (int i = 0; i < purchaseTicketDTO.getAmount(); i++) {
            Ticket ticket = createTicket(event, customer, finalPrice.divide(BigDecimal.valueOf(purchaseTicketDTO.getAmount())));
            ticket = ticketRepository.save(ticket);
            publishTicketPurchaseEvent(ticket, event);
        }

        eventRepository.save(event);
    }

    private void validateTicketAvailability(Event event, int requestedAmount) {
        if (event.getAvailableTickets() < requestedAmount) {
            throw new InsufficientTicketsException(
                    String.format("Not enough tickets available. Requested: %d, Available: %d",
                            requestedAmount, event.getAvailableTickets())
            );
        }
    }

    private BigDecimal calculateTicketPrice(Event event) {
        BigDecimal basePrice = event.getBasePrice();

        if (event.isThresholdReached()) {
            return basePrice.multiply(BigDecimal.valueOf(1.2));
        }

        return basePrice;
    }

    private Ticket createTicket(Event event, Customer customer, BigDecimal finalPrice) {
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setStatusUsed(false);
        ticket.setBasePrice(finalPrice);
        return ticket;
    }

    private void publishTicketPurchaseEvent(Ticket ticket, Event event) {
        eventPublisher.publishEvent(new TicketPurchaseEvent(this, ticket, event));
    }
}


