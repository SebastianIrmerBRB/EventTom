package API.EventTom.services.interfaces;


import API.EventTom.DTO.EventDTO;

import java.util.List;

public interface IEventService {


    List<EventDTO> getAllEvents();
    EventDTO getEventById(long id);

}
