package API.EventTom.repositories;

import API.EventTom.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    public Ticket findByTicketId(long ticketId);
}
