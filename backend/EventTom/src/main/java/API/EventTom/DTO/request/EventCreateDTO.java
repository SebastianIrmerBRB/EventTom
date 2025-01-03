package API.EventTom.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDTO {
    private String title;
    private String location;
    private LocalDateTime dateOfEvent;
    private int totalTickets;
    private int thresholdValue;
    private BigDecimal basePrice;
    private Long managerId;

}
