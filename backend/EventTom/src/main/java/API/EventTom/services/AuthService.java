package API.EventTom.services;

import API.EventTom.DTO.request.EmployeeRegisterRequest;
import API.EventTom.DTO.request.LoginRequest;
import API.EventTom.DTO.request.CustomerRegisterRequest;
import API.EventTom.DTO.response.LoginResponse;
import API.EventTom.DTO.response.RegisterResponse;
import API.EventTom.models.*;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.repositories.EmployeeRepository;
import API.EventTom.repositories.RoleRepository;
import API.EventTom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Transactional
    public ResponseEntity<?> register(CustomerRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("User already exists with email: " + request.getEmail());
        }

        // Create customer with NONE role
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setCustomerNumber(generateCustomerNumber());

        // Add NONE role, Customer's sollen nichts können außer kaufen, was unter None abgedeckt wird
        Role noneRole = roleRepository.findByName(Roles.NONE)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        customer.getRoles().add(noneRole);

        User userEntity = customerRepository.save(customer);

        return ResponseEntity.ok(new RegisterResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                "User registered successfully"
        ));
    }

    // @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @Transactional
    public ResponseEntity<?> registerEmployee(EmployeeRegisterRequest request) {
        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body("User already exists with email: " + request.getEmail());
            }

            Employee employee = new Employee();
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setEmail(request.getEmail());
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
            employee.setEmployeeNumber(generateEmployeeNumber());

            Set<Role> roles = new HashSet<>();
            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                for (Roles roleEnum : request.getRoles()) {
                    Role role = roleRepository.findByName(roleEnum)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Role not found: " + roleEnum));
                    roles.add(role);
                }
            } else {
                Role defaultRole = roleRepository.findByName(Roles.NONE)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Default role not found"));
                roles.add(defaultRole);
            }

            employee.setRoles(roles);
            Employee savedEmployee = employeeRepository.save(employee);

            return ResponseEntity.ok(new RegisterResponse(
                    savedEmployee.getId(),
                    savedEmployee.getEmail(),
                    "Employee registered successfully"
            ));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error registering employee");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @Transactional
    public ResponseEntity<?> assignRole(Long userId, Roles role) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            Role roleEntity = roleRepository.findByName(role)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

            user.getRoles().add(roleEntity);
            userRepository.save(user);

            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error assigning role");
        }
    }

    public ResponseEntity<?> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(new LoginResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getRoles().stream()
                            .map(role -> role.getName().toString())
                            .collect(Collectors.toSet()),
                    "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Invalid email or password");
        }
    }

    @Transactional
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok()
                .body("Logged out successfully");
    }

    private String generateCustomerNumber() {
        return "C" + System.currentTimeMillis();
    }

    private String generateEmployeeNumber() {
        return "E" + System.currentTimeMillis();
    }
}