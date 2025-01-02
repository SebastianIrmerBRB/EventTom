package API.EventTom.controller.events;

import API.EventTom.DTO.EventDTO;
import API.EventTom.services.events.IEventQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/events")
public class EventQueryController {
    private final IEventQueryService eventQueryService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventQueryService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable long id) {
        return ResponseEntity.ok(eventQueryService.getEventById(id));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<EventDTO>> getEventsByManager(@PathVariable long managerId) {
        return ResponseEntity.ok(eventQueryService.getEventsByManagerId(managerId));
    }
}
