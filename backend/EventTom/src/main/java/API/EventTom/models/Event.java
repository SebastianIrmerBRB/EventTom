package API.EventTom.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
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

    @Column(name = "date", nullable = false)
    private LocalDateTime dateOfEvent;

    @Column(name = "location", nullable = false)
    private String location;

    // absolut maximum of allowed tickets
    @Column(name = "max_total_tickets")
    private int maxTotalTickets;

    // value for price increase
    @Column(name = "threshold_value")
    private int thresholdValue;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Employee creator;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_managers",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> managers = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    @Transient
    public boolean isThresholdReached() {
        return tickets.size() >= thresholdValue;
    }

    @Transient
    public int getAvailableTickets() {
        return maxTotalTickets - tickets.size();
    }

    @Transient
    public int getTotalSoldTickets() {
        return tickets.size();
    }

}



