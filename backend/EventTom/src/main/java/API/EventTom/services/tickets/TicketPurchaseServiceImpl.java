package API.EventTom.services.tickets;

import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.DTO.response.TicketPurchaseResponseDTO;
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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class TicketPurchaseServiceImpl implements ITicketPurchaseService {
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final IVoucherUsageService voucherUsageService;

    @Override
    public BigDecimal calculateTotalPrice(PurchaseTicketDTO purchaseTicketDTO, Long userId) {
        Event event = eventRepository.findById(purchaseTicketDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(purchaseTicketDTO.getEventId()));

        validateTicketAvailability(event, purchaseTicketDTO.getAmount());

        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found for user ID: " + userId));

        BigDecimal currentPrice = calculateTicketPrice(event);
        BigDecimal totalPrice = currentPrice.multiply(BigDecimal.valueOf(purchaseTicketDTO.getAmount()));

        if (purchaseTicketDTO.getVoucherCode() != null && !purchaseTicketDTO.getVoucherCode().isEmpty()) {
            Voucher voucher = voucherUsageService.validateVoucher(purchaseTicketDTO.getVoucherCode());
            totalPrice = voucherUsageService.calculateDiscountedAmount(totalPrice, voucher);
        }

        return totalPrice;
    }

    @Override
    @Transactional
    public TicketPurchaseResponseDTO purchaseTicket(PurchaseTicketDTO purchaseTicketDTO, Long userId) {
        Event event = eventRepository.findById(purchaseTicketDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(purchaseTicketDTO.getEventId()));

        validateTicketAvailability(event, purchaseTicketDTO.getAmount());

        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found for user ID: " + userId));

        BigDecimal finalPrice = calculateTotalPrice(purchaseTicketDTO, userId);

        if (purchaseTicketDTO.getVoucherCode() != null && !purchaseTicketDTO.getVoucherCode().isEmpty()) {
            voucherUsageService.useVoucherForPurchase(
                    purchaseTicketDTO.getVoucherCode(),
                    customer.getId(),
                    finalPrice
            );
        }

        BigDecimal pricePerTicket = finalPrice.divide(BigDecimal.valueOf(purchaseTicketDTO.getAmount()),
                2, RoundingMode.HALF_UP);

        List<Long> ticketIds = new ArrayList<>();

        for (int i = 0; i < purchaseTicketDTO.getAmount(); i++) {
            Ticket ticket = createTicket(event, customer, pricePerTicket);
            ticket = ticketRepository.save(ticket);
            ticketIds.add(ticket.getId());
            publishTicketPurchaseEvent(ticket, event);
        }

        eventRepository.save(event);

        return new TicketPurchaseResponseDTO(
                event.getTitle(),
                event.getDateOfEvent(),
                purchaseTicketDTO.getAmount(),
                finalPrice,
                pricePerTicket,
                ticketIds,
                event.getLocation()
        );
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


