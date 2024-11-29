package API.EventTom.observers;

import API.EventTom.models.Event;
import API.EventTom.models.Ticket;
import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;


@Getter
public class TicketPurchaseEvent extends ApplicationEvent {
    private final Ticket ticket;
    private final Event event;
    private final long remainingTickets;
    private final double soldPercentage;

    public TicketPurchaseEvent(Object source, Ticket ticket, Event event) {
        super(source);
        this.ticket = ticket;
        this.event = event;
        this.remainingTickets = event.getTotalTickets() - event.getTotalSoldTickets();
        this.soldPercentage = (double) event.getTotalSoldTickets() / event.getTotalTickets() * 100;
    }
}
