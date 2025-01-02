package API.EventTom.services.events;


import API.EventTom.DTO.EventDTO;

import java.util.List;

public interface IEventQueryService {


    List<EventDTO> getAllEvents();
    EventDTO getEventById(long id);
    List<EventDTO> getEventsByManagerId(long managerId);
}
