package API.EventTom.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false)  // Changed field name and added explicit column name
    private boolean isRead = false;

    @Column(nullable = false)
    private String notificationType; // e.g., "TICKET_PURCHASE", "EVENT_UPDATE"
}