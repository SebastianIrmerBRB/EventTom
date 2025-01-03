package API.EventTom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    public String title;
    public LocalDateTime dateOfEvent;
    public long soldTickets;
    public long thresholdValue;
    public BigDecimal basePrice;
    public long location;
}
