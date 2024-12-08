package API.EventTom.services.tickets.interfaces;

import API.EventTom.DTO.request.PurchaseTicketDTO;

public interface ITicketPurchaseService {
    void purchaseTicket(PurchaseTicketDTO purchaseTicketDTO);
}