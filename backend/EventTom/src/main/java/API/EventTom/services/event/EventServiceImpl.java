package API.EventTom.services.event;

import API.EventTom.DTO.EventDTO;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Event;
import API.EventTom.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    EventRepository eventRepository;
    StandardDTOMapper standardDTOMapper;
    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(standardDTOMapper::mapEventToEventDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event with ID could not be found"));
        return standardDTOMapper.mapEventToEventDTO(event);
    }


}
