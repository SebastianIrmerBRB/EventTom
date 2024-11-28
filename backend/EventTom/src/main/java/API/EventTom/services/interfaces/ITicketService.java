package API.EventTom.services.interfaces;

import API.EventTom.DTO.TicketDTO;

import java.util.List;

public interface ITicketService {

    public List<TicketDTO> getTicketByEventId(Long id);
    public List<TicketDTO> getByCustomerId(String customerId);
    public TicketDTO getTicketById(long ticketId);

}
