package API.EventTom.controller.tickets;

import API.EventTom.DTO.TicketDTO;
import API.EventTom.services.tickets.interfaces.ITicketQueryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/tickets/query")
@RequiredArgsConstructor
public class TicketQueryController {
    private final ITicketQueryService ticketQueryService;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketDTO>> getEventTickets(@PathVariable Long eventId) {
        List<TicketDTO> tickets = ticketQueryService.getTicketsByEventId(eventId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketDTO>> getCustomerTickets(@PathVariable String customerId) {
        List<TicketDTO> tickets = ticketQueryService.getTicketsByCustomerId(customerId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable long id) {
        TicketDTO ticketDTO = ticketQueryService.getTicketById(id);
        return ResponseEntity.ok(ticketDTO);
    }
}