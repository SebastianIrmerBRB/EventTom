package API.EventTom.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateDTO {
    private String title;
    private LocalDateTime dateOfEvent;
    private int totalTickets;
    private int thresholdValue;
    private BigDecimal basePrice;
}