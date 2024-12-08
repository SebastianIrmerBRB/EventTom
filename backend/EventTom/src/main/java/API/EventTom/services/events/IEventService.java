package API.EventTom.services.events;


import API.EventTom.DTO.EventDTO;

import java.util.List;

public interface IEventService {


    List<EventDTO> getAllEvents();
    EventDTO getEventById(long id);

}
