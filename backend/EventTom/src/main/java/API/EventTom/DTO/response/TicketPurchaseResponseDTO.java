package API.EventTom.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketPurchaseResponseDTO {
    private String eventTitle;
    private LocalDateTime eventDate;
    private int purchasedTickets;
    private BigDecimal totalPrice;
    private BigDecimal pricePerTicket;
    private List<Long> ticketIds;
    private String location;
}
