package API.EventTom.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Employee {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    @PreUpdate
    private void validateUserType() {
        if (user.getUserType() != UserType.EMPLOYEE) {
            throw new IllegalStateException("User must be of type EMPLOYEE");
        }
    }


    @Column(name = "employee_number", unique = true, nullable = false)
    private String employeeNumber;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Event> createdEvents = new ArrayList<>();

    // Events managed by this employee
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Event> managedEvents = new ArrayList<>();
}
