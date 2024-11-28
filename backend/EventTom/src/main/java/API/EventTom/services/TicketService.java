package API.EventTom.services;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.DTO.TicketDTO;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Customer;
import API.EventTom.models.Event;
import API.EventTom.models.Ticket;
import API.EventTom.repositories.TicketRepository;
import API.EventTom.services.interfaces.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService implements ITicketService {

    TicketRepository ticketRepository;
    StandardDTOMapper standardDTOMapper;
    @Override
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(standardDTOMapper::mapTicketToTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getByCustomerId(String customerId) {
        List<Ticket> tickets = ticketRepository.findAllTicketsByCustomerNumber(customerId);
        return tickets.stream()
                .map(standardDTOMapper::mapTicketToTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDTO getTicketById(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new RuntimeException("Ticket for ID could not be found"));
        return standardDTOMapper.mapTicketToTicketDTO(ticket);
    }

}
