package API.EventTom.DTO.request;


import API.EventTom.models.Event;
import API.EventTom.models.Voucher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTicketDTO {

    Long eventId;
    Long ticketId;
    int amount;
    String customerNumber;
    Long voucherId;
}
