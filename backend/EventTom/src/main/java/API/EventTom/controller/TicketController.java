package API.EventTom.controller;

import API.EventTom.DTO.TicketDTO;
import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.services.interfaces.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/tickets")
public class TicketController {

    ITicketService ticketService;

    // TODO: WRITE DTO FOR EACH REQUEST
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketDTO>> getEventTickets(@PathVariable Long eventId) {
        List<TicketDTO> tickets = ticketService.getTicketsByEventId(eventId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketDTO>> getCustomerTickets(@PathVariable String customerId) {
        List<TicketDTO> vouchers = ticketService.getTicketsByCustomerId(customerId);
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable long id) {
        TicketDTO ticketDTO = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticketDTO);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchaseTicket(@RequestBody PurchaseTicketDTO purchaseTicketDTO) {
        ticketService.purchaseTicket(purchaseTicketDTO);
        return ResponseEntity.ok().build();
    }

}
