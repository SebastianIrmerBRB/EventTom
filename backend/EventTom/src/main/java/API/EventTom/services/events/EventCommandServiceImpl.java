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
import API.EventTom.services.notifications.WebSocketNotificationService;
import API.EventTom.services.notifications.WebsiteNotificationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventCommandServiceImpl implements IEventCommandService {
    private final EventRepository eventRepository;
    private final EmployeeRepository employeeRepository;
    private final StandardDTOMapper standardDTOMapper;
    private final WebsiteNotificationServiceImpl dbNotificationService;
    private final WebSocketNotificationService wsNotificationService;

    @Override
    @Transactional
    public EventDTO createEvent(EventCreateDTO eventCreateDTO, Long userId) {
        // Find all managers
        List<Employee> managers = eventCreateDTO.getManagerIds().stream()
                .map(id -> employeeRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + id)))
                .collect(Collectors.toList());

        // Find the creator (assuming creator ID is separate from manager IDs)
        Employee creator = employeeRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator not found"));

        Event event = new Event();
        event.setTitle(eventCreateDTO.getTitle());
        event.setDateOfEvent(eventCreateDTO.getDateOfEvent());
        event.setMaxTotalTickets(eventCreateDTO.getTotalTickets());
        event.setThresholdValue(eventCreateDTO.getThresholdValue());
        event.setBasePrice(eventCreateDTO.getBasePrice());
        event.setManagers(managers);
        event.setCreator(creator);
        event.setLocation(eventCreateDTO.getLocation());

        Event savedEvent = eventRepository.save(event);
        EventDTO eventDTO = standardDTOMapper.mapEventToEventDTO(savedEvent);

        wsNotificationService.notifyEventCreated(eventDTO);

        return eventDTO;
    }
    @Override
    @Transactional
    public EventDTO updateEvent(long id, EventUpdateDTO eventUpdateDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setTitle(eventUpdateDTO.getTitle());
        event.setDateOfEvent(eventUpdateDTO.getDateOfEvent());
        event.setMaxTotalTickets(eventUpdateDTO.getTotalTickets());
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