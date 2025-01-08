package API.EventTom.services.notifications;

import API.EventTom.DTO.EventDTO;
import API.EventTom.models.Employee;
import API.EventTom.models.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyEventCreated(EventDTO event) {
        System.out.println("test1235");

        messagingTemplate.convertAndSend("/topic/events/new", event);
    }

    public void notifyEventManagersTicketSale(Event event) {
        Map<String, Object> notification = Map.of(
                "eventId", event.getEventId(),
                "soldTickets", event.getTickets(),
                "threshold", event.getThresholdValue(),
                "deviation", event.getAvailableTickets()
        );

        for (Employee manager : event.getManagers()) {
            messagingTemplate.convertAndSend(
                    "/topic/managers/" + manager.getId() + "/events/" + event.getEventId(),
                    notification
            );
        }
    }

}