package API.EventTom.services;

import API.EventTom.DTO.EventDTO;
import API.EventTom.services.interfaces.IEventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService implements IEventService {

    @Override
    public List<EventDTO> getAllEvents() {
        return List.of();
    }

    @Override
    public EventDTO getEventById(int id) {
        return null;
    }
}
