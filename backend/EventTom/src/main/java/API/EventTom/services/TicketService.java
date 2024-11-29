package API.EventTom.services;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.DTO.EventDTO;
import API.EventTom.DTO.TicketDTO;
import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Customer;
import API.EventTom.models.Event;
import API.EventTom.models.Ticket;
import API.EventTom.observers.TicketPurchaseEvent;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.repositories.EventRepository;
import API.EventTom.repositories.TicketRepository;
import API.EventTom.services.interfaces.ITicketService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService implements ITicketService {

    TicketRepository ticketRepository;
    StandardDTOMapper standardDTOMapper;
    EventRepository eventRepository;
    CustomerRepository customerRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<TicketDTO> getTicketsByEventId(Long id) {
        List<Ticket> tickets = ticketRepository.findAllByEventId(id);
        return tickets.stream()
                .map(standardDTOMapper::mapTicketToTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getTicketsByCustomerId(String customerId) {
        List<Ticket> tickets = ticketRepository.findAllTicketsByCustomerNumber(customerId);
        return tickets.stream()
                .map(standardDTOMapper::mapTicketToTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDTO getTicketById(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new RuntimeException("Ticket for ID could not be found"));
        return standardDTOMapper.mapTicketToTicketDTO(ticket);
    }

    @Override
    @Transactional
    public void purchaseTicket(PurchaseTicketDTO purchaseTicketDTO) {
        // Get the event and customer
        Event event = eventRepository.findById(purchaseTicketDTO.getEventId())
                .orElseThrow(() -> new RuntimeException("Could not find Event"));
        Customer customer = customerRepository.findCustomerByCustomerNumber(purchaseTicketDTO.getCustomerNumber())
                .orElseThrow(() -> new RuntimeException("Could not find Customer"));

        // Create and save the ticket
        // hier weiß ich nich wie das gemacht werden soll ...
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setPurchaseDate(LocalDateTime.now());

        // Update event's sold tickets count
        event.setTotalSoldTickets(event.getTotalSoldTickets() + 1);

        // Save the ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // Publish the event
        eventPublisher.publishEvent(new TicketPurchaseEvent(
                this,
                savedTicket,
                event
        ));
    }

    private String generateTicketNumber(Event event) {
        // Implement ticket number generation logic
        return String.format("T-%d-%d", event.getEventId(), System.currentTimeMillis());
    }

}
