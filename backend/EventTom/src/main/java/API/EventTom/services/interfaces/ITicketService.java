package API.EventTom.services.interfaces;

import API.EventTom.DTO.TicketDTO;
import API.EventTom.DTO.request.PurchaseTicketDTO;

import java.util.List;

public interface ITicketService {

    List<TicketDTO> getTicketsByEventId(Long id);
    List<TicketDTO> getTicketsByCustomerId(String customerId);
    TicketDTO getTicketById(long ticketId);

    void purchaseTicket(PurchaseTicketDTO purchaseTicketDTO);
}
