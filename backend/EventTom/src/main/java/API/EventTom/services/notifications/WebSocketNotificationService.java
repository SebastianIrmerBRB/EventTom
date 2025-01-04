package API.EventTom.services.notifications;

import API.EventTom.DTO.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final CorsConfigurationSource corsConfigurationSource;

    public void notifyEventCreated(EventDTO event) {
        System.out.println("test1235");

        messagingTemplate.convertAndSend("/topic/events/new", event);
    }

    public void notifyTicketsSold(Long eventId, int availableTickets) {
        System.out.println("test");
        messagingTemplate.convertAndSend("/topic/events/" + eventId + "/tickets",
                Map.of("eventId", eventId, "availableTickets", availableTickets));
    }

    public void notifyEventManagersTicketSale(Long eventId, int soldTickets, int threshold) {
        messagingTemplate.convertAndSend("/topic/management/events/" + eventId + "/sales",
                Map.of(
                        "eventId", eventId,
                        "soldTickets", soldTickets,
                        "threshold", threshold
                ));
    }
}