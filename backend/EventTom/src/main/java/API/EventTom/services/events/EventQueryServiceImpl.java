package API.EventTom.services.events;

import API.EventTom.DTO.EventDTO;
import API.EventTom.exceptions.RuntimeExceptions.ResourceNotFoundException;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Event;
import API.EventTom.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventQueryServiceImpl implements IEventQueryService {

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
        return eventRepository.findById(id)
                .map(standardDTOMapper::mapEventToEventDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<EventDTO> getEventsByManagerId(long managerId) {
        return eventRepository.findByManagers_Id(managerId).stream()
                .map(standardDTOMapper::mapEventToEventDTO)
                .collect(Collectors.toList());
    }
}
