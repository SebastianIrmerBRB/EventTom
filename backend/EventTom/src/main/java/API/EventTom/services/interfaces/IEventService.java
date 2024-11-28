package API.EventTom.services.interfaces;


import API.EventTom.DTO.EventDTO;

import java.util.List;

public interface IEventService {


    public List<EventDTO> getAllEvents();
    public EventDTO getEventById(int id);

}
