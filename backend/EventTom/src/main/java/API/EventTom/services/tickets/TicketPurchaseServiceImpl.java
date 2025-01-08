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
import API.EventTom.services.notifications.INotificationService;
import API.EventTom.services.notifications.WebSocketNotificationService;
import API.EventTom.services.notifications.WebsiteNotificationServiceImpl;
import API.EventTom.services.tickets.interfaces.ITicketCreationService;
import API.EventTom.services.tickets.interfaces.ITicketPriceCalculator;
import API.EventTom.services.tickets.interfaces.ITicketPurchaseService;
import API.EventTom.services.tickets.interfaces.ITicketValidator;
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
@Slf4j
public class TicketPurchaseServiceImpl implements ITicketPurchaseService {
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final IVoucherUsageService voucherUsageService;
    private final ITicketPriceCalculator priceCalculator;
    private final ITicketValidator ticketValidator;
    private final ITicketCreationService ticketCreationService;
    private final WebSocketNotificationService webSocketService;

    @Override
    public BigDecimal calculateTotalPrice(PurchaseTicketDTO purchaseTicketDTO, Long userId) {
        Event event = findEvent(purchaseTicketDTO.getEventId());
        ticketValidator.validatePurchaseRequest(event, purchaseTicketDTO);

        return priceCalculator.calculateTotalPrice(
                event,
                purchaseTicketDTO.getAmount(),
                purchaseTicketDTO.getVoucherCodes()
        );
    }

    @Override
    @Transactional
    public TicketPurchaseResponseDTO purchaseTicket(PurchaseTicketDTO purchaseTicketDTO, Long userId) {
        Event event = findEvent(purchaseTicketDTO.getEventId());
        Customer customer = findCustomer(userId);

        ticketValidator.validatePurchaseRequest(event, purchaseTicketDTO);

        BigDecimal baseTicketPrice = priceCalculator.calculateBasePrice(event);
        List<Voucher> validatedVouchers = voucherUsageService.validateVouchers(purchaseTicketDTO.getVoucherCodes());
        BigDecimal totalVoucherDiscount = voucherUsageService.calculateTotalDiscount(validatedVouchers);
        voucherUsageService.markVouchersAsUsed(validatedVouchers, customer.getId());

        PurchaseResult purchaseResult = ticketCreationService.processTicketPurchase(
                event, customer, purchaseTicketDTO.getAmount(),
                baseTicketPrice, totalVoucherDiscount
        );

        eventRepository.save(event);
        webSocketService.notifyEventManagersTicketSale(event);

        return createPurchaseResponse(event, purchaseResult, baseTicketPrice);
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Customer findCustomer(Long userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found for user ID: " + userId));
    }

    private TicketPurchaseResponseDTO createPurchaseResponse(
            Event event, PurchaseResult result, BigDecimal baseTicketPrice) {
        return new TicketPurchaseResponseDTO(
                event.getTitle(),
                event.getDateOfEvent(),
                result.ticketIds().size(),
                result.totalPrice(),
                baseTicketPrice,
                result.ticketIds(),
                event.getLocation()
        );
    }

}