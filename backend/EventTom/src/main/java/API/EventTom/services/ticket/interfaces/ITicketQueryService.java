package API.EventTom.services.ticket.interfaces;

import API.EventTom.DTO.TicketDTO;

import java.util.List;

public interface ITicketQueryService {

    List<TicketDTO> getTicketsByEventId(Long id);
    List<TicketDTO> getTicketsByCustomerId(String customerId);
    TicketDTO getTicketById(long ticketId);

}
