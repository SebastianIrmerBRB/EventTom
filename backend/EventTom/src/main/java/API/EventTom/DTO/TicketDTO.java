package API.EventTom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    public long finalPrice;
    public LocalDateTime purchaseDate;
    public boolean statusUsed;
    public long eventId;
    public long customerId;

}
