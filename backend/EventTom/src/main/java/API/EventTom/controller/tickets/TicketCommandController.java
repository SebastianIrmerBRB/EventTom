package API.EventTom.controller.tickets;

import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.DTO.response.TicketPurchaseResponseDTO;
import API.EventTom.config.AuthenticatedUserId;
import API.EventTom.services.tickets.interfaces.ITicketPurchaseService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("api/tickets/command")
@RequiredArgsConstructor
public class TicketCommandController {
    private final ITicketPurchaseService ticketPurchaseService;

    @PostMapping("/calculate-price")
    public ResponseEntity<BigDecimal> calculatePrice(@RequestBody PurchaseTicketDTO purchaseTicketDTO, @AuthenticatedUserId Long userId) {
        BigDecimal totalPrice = ticketPurchaseService.calculateTotalPrice(purchaseTicketDTO, userId);
        return ResponseEntity.ok(totalPrice);
    }

    @PostMapping("/purchase")
    public ResponseEntity<TicketPurchaseResponseDTO> purchaseTicket(
            @RequestBody PurchaseTicketDTO purchaseTicketDTO,
            @AuthenticatedUserId Long userId) {
        TicketPurchaseResponseDTO response = ticketPurchaseService.purchaseTicket(purchaseTicketDTO, userId);
        return ResponseEntity.ok(response);
    }
}