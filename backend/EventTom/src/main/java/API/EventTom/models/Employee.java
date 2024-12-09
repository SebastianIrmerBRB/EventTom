package API.EventTom.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// Veränderung basierend auf: https://claude.ai/chat/0b79732a-0b0c-446f-8283-cb182582fd42

@Entity
@Data
public class Employee {
    @Id
    private Long id;

    @Column(name = "employee_number", unique = true, nullable = false)
    private String employeeNumber;

    // Ich meine, dass sollte separat in beiden existieren, da eventuell unterschiedlich
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    private User user;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Event> createdEvents = new ArrayList<>();

    // Events managed by this employee
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Event> managedEvents = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


}
