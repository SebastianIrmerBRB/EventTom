package API.EventTom.services.interfaces;

import API.EventTom.DTO.TicketDTO;

import java.util.List;

public interface ITicketService {

    public List<TicketDTO> getAllTickets();
    public List<TicketDTO> getByCustomerId(int customerId);
    public TicketDTO getTicketById(int eventId);

}
