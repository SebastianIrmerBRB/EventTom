package API.EventTom.models;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@DynamicUpdate
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserProfile userProfile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Utility method to get user's highest role
    public Optional<Roles> getHighestRole() {
        return roles.stream()
                .map(Role::getName)
                .min(Comparator.comparingInt(Enum::ordinal));
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @PrePersist
    @PreUpdate
    protected void validateRoles() {
        if (userType == UserType.CUSTOMER) {
            boolean hasEmployeeOnlyRoles = roles.stream()
                    .map(Role::getName)
                    .anyMatch(Roles::isEmployeeOnly);

            if (hasEmployeeOnlyRoles) {
                throw new IllegalStateException("Customers cannot have employee-only roles");
            }
        }
    }

    public void addRole(Role role) {
        if (userType == UserType.CUSTOMER && Roles.isEmployeeOnly(role.getName())) {
            throw new IllegalStateException("Cannot assign employee-only role to a customer");
        }
        roles.add(role);
    }
}