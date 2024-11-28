package API.EventTom.controller;

import API.EventTom.DTO.EmployeeDTO;
import API.EventTom.DTO.TicketDTO;
import API.EventTom.DTO.VoucherDTO;
import API.EventTom.models.Ticket;
import API.EventTom.services.interfaces.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/tickets")
public class TicketController {

    ITicketService ticketService;

    // TODO: WRITE DTO FOR EACH REQUEST
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketDTO>> getAllTickets(@PathVariable Long eventId) {
        List<TicketDTO> tickets = ticketService.getTicketByEventId(eventId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketDTO>> getCustomerTickets(@PathVariable String customerId) {
        List<TicketDTO> vouchers = ticketService.getByCustomerId(customerId);
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable long id) {
        TicketDTO ticketDTO = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticketDTO);
    }

}
