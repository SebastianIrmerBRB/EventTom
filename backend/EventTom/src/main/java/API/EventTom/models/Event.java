package API.EventTom.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date")
    private LocalDateTime dateOfEvent;

    @Column(name = "total_tickets")
    private long totalTickets;

    @Column(name = "sold_tickets")
    private long totalSoldTickets;

    @Column(name = "threshold_value")
    private long thresholdValue;

    @Column(name = "base_price")
    private long basePrice;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

}



