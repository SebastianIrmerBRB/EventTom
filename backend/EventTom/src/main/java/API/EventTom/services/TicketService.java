package API.EventTom.services;

import API.EventTom.DTO.TicketDTO;
import API.EventTom.services.interfaces.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketService implements ITicketService {
    @Override
    public List<TicketDTO> getAllTickets() {
        return List.of();
    }

    @Override
    public List<TicketDTO> getByCustomerId(int customerId) {
        return List.of();
    }

    @Override
    public TicketDTO getTicketById(int eventId) {
        return null;
    }
}
