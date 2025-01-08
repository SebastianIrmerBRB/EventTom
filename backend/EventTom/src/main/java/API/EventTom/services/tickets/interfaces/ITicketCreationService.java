package API.EventTom.services.tickets.interfaces;

import API.EventTom.models.Customer;
import API.EventTom.models.Event;
import API.EventTom.services.tickets.PurchaseResult;

import java.math.BigDecimal;

public interface ITicketCreationService {
    PurchaseResult processTicketPurchase(Event event, Customer customer,
                                         int amount, BigDecimal basePrice,
                                         BigDecimal totalVoucherDiscount);
}
