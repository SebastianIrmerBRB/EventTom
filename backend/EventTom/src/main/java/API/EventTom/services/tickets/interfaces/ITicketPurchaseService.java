package API.EventTom.services.tickets.interfaces;

import API.EventTom.DTO.request.PurchaseTicketDTO;
import API.EventTom.DTO.response.TicketPurchaseResponseDTO;

import java.math.BigDecimal;

public interface ITicketPurchaseService {
    TicketPurchaseResponseDTO purchaseTicket(PurchaseTicketDTO purchaseTicketDTO, Long userId);
    BigDecimal calculateTotalPrice(PurchaseTicketDTO purchaseTicketDTO, Long userId);
}