package API.EventTom.controller;

import API.EventTom.DTO.TicketDTO;
import API.EventTom.services.interfaces.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TicketController {

    ITicketService ticketService;

    // TODO: WRITE DTO FOR EACH REQUEST

    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }
}
