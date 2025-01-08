package API.EventTom.controller.events;
import API.EventTom.DTO.EventDTO;
import API.EventTom.DTO.request.EventCreateDTO;
import API.EventTom.DTO.request.EventUpdateDTO;
import API.EventTom.config.AuthenticatedUserId;
import API.EventTom.services.events.IEventCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("api/events")
public class EventCommandController {
    private final IEventCommandService eventCommandService;

    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent( @RequestBody EventCreateDTO eventCreateDTO, @AuthenticatedUserId Long userId) {
        return new ResponseEntity<>(eventCommandService.createEvent(eventCreateDTO, userId), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable long id, @RequestBody EventUpdateDTO eventUpdateDTO) {
        return ResponseEntity.ok(eventCommandService.updateEvent(id, eventUpdateDTO));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        eventCommandService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}