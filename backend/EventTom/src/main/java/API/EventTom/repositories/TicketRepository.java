package API.EventTom.repositories;

import API.EventTom.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
     //     @Query("SELECT v FROM Voucher v WHERE v.customer.customerNumber = :customerNumber")
     @Query("SELECT t FROM Ticket t WHERE t.customer.customerNumber = :customerNumber")
     List<Ticket> findAllTicketsByCustomerNumber(String id);
}
