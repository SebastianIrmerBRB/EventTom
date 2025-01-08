package API.EventTom.services.events;

import API.EventTom.DTO.EventDTO;
import API.EventTom.DTO.request.EventCreateDTO;
import API.EventTom.DTO.request.EventUpdateDTO;
import org.springframework.transaction.annotation.Transactional;

public interface IEventCommandService {
    @Transactional
    EventDTO createEvent(EventCreateDTO eventCreateDTO, Long userId);

    EventDTO updateEvent(long id, EventUpdateDTO eventUpdateDTO);


    void deleteEvent(long id);
}