package API.EventTom.services.ticket.interfaces;

import API.EventTom.DTO.request.PurchaseTicketDTO;

public interface ITicketPurchaseService {
    void purchaseTicket(PurchaseTicketDTO purchaseTicketDTO);
}