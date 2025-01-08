package API.EventTom.DTO.request;


import API.EventTom.models.Event;
import API.EventTom.models.Voucher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTicketDTO {

    Long eventId;
    int amount;
    private List<String> voucherCodes; // Changed from single String to List

}
