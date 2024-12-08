package API.EventTom.services.tickets;

import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.exceptions.RuntimeExceptions.CustomerNotFoundException;
import API.EventTom.exceptions.RuntimeExceptions.EventNotFoundException;
import API.EventTom.models.Customer;
import API.EventTom.models.Event;
import API.EventTom.models.Ticket;
import API.EventTom.observers.TicketPurchaseEvent;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.repositories.EventRepository;
import API.EventTom.repositories.TicketRepository;
import API.EventTom.services.tickets.interfaces.ITicketPurchaseService;
import API.EventTom.services.vouchers.IVoucherValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class TicketPurchaseServiceImpl implements ITicketPurchaseService {
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final IVoucherValidator voucherValidator;

    @Override
    @Transactional
    public void purchaseTicket(PurchaseTicketDTO purchaseTicketDTO) {
        Event event = eventRepository.findById(purchaseTicketDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(purchaseTicketDTO.getEventId()));

        Customer customer = customerRepository.findCustomerByCustomerNumber(purchaseTicketDTO.getCustomerNumber())
                .orElseThrow(() -> new CustomerNotFoundException(purchaseTicketDTO.getCustomerNumber()));

        // validate if ticket can be valid?

        if (purchaseTicketDTO.getVoucherCode() != null) {
            voucherValidator.validateVoucher(
                    purchaseTicketDTO.getVoucherCode(),
                    customer.getCustomerNumber(),
                    event.getBasePrice()
            );
        }

        long finalPrice = voucherValidator.calculateDiscountedAmount(
                purchaseTicketDTO.getVoucherCode(),
                event.getBasePrice()
        );

        Ticket ticket = createTicket(event, customer, finalPrice);
        Ticket savedTicket = ticketRepository.save(ticket);

        updateEventTicketCount(event);
        publishTicketPurchaseEvent(savedTicket, event);
    }


    private Ticket createTicket(Event event, Customer customer, long finalPrice) {
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setStatusUsed(false);
        ticket.setFinalPrice(finalPrice);
        return ticket;
    }


    private void updateEventTicketCount(Event event) {
        event.setTotalSoldTickets(event.getTotalSoldTickets() + 1);
    }

    private void publishTicketPurchaseEvent(Ticket ticket, Event event) {
        eventPublisher.publishEvent(new TicketPurchaseEvent(this, ticket, event));
    }
}