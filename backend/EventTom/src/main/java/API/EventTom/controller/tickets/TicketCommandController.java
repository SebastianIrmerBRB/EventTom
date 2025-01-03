package API.EventTom.controller.tickets;

import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.services.tickets.interfaces.ITicketPurchaseService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/tickets/command")
@RequiredArgsConstructor
public class TicketCommandController {
    private final ITicketPurchaseService ticketPurchaseService;

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchaseTicket(@RequestBody PurchaseTicketDTO purchaseTicketDTO) {
        ticketPurchaseService.purchaseTicket(purchaseTicketDTO);
        return ResponseEntity.ok().build();
    }
}
