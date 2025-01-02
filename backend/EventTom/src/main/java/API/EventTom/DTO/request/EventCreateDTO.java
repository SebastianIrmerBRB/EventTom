package API.EventTom.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDTO {
    private String title;
    private LocalDateTime dateOfEvent;
    private long totalTickets;
    private long thresholdValue;
    private long basePrice;
    private Long managerId;
}
