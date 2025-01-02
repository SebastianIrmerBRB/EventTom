package API.EventTom.services.events;
import API.EventTom.DTO.EventDTO;
import API.EventTom.DTO.request.EventCreateDTO;
import API.EventTom.DTO.request.EventUpdateDTO;
import API.EventTom.exceptions.RuntimeExceptions.ResourceNotFoundException;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Employee;
import API.EventTom.models.Event;
import API.EventTom.repositories.EmployeeRepository;
import API.EventTom.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EventCommandServiceImpl implements IEventCommandService {
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final StandardDTOMapper standardDTOMapper;

    @Override
    @Transactional
    public EventDTO createEvent(EventCreateDTO eventCreateDTO) {
        Employee manager = employeeRepository.findById(eventCreateDTO.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        Event event = new Event();

        event.setTitle(eventCreateDTO.getTitle());
        event.setDateOfEvent(eventCreateDTO.getDateOfEvent());
        event.setTotalTickets(eventCreateDTO.getTotalTickets());
        event.setTotalSoldTickets(0);
        event.setThresholdValue(eventCreateDTO.getThresholdValue());
        event.setBasePrice(eventCreateDTO.getBasePrice());
        event.setManager(manager);
        event.setCreator(manager);
        Event savedEvent = eventRepository.save(event);
        return standardDTOMapper.mapEventToEventDTO(savedEvent);
    }

    @Override
    @Transactional
    public EventDTO updateEvent(long id, EventUpdateDTO eventUpdateDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setTitle(eventUpdateDTO.getTitle());
        event.setDateOfEvent(eventUpdateDTO.getDateOfEvent());
        event.setTotalTickets(eventUpdateDTO.getTotalTickets());
        event.setThresholdValue(eventUpdateDTO.getThresholdValue());
        event.setBasePrice(eventUpdateDTO.getBasePrice());

        Event updatedEvent = eventRepository.save(event);
        return standardDTOMapper.mapEventToEventDTO(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found");
        }
        eventRepository.deleteById(id);
    }
}