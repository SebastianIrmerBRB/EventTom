package API.EventTom.controller;

import API.EventTom.DTO.EventDTO;
import API.EventTom.services.interfaces.IEventService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class EventController{

    IEventService eventService;

    // TODO: WRITE DTO FOR EACH REQUEST

    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
