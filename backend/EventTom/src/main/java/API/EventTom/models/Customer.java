package API.EventTom.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    @PreUpdate
    private void validateUserType() {
        if (user.getUserType() != UserType.CUSTOMER) {
            throw new IllegalStateException("User must be of type CUSTOMER");
        }
    }

    @Column(name = "customer_number", unique = true, nullable = false)
    private String customerNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Voucher> vouchers = new ArrayList<>();
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

}