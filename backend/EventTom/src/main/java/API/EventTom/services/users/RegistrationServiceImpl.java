package API.EventTom.services.users;

import API.EventTom.DTO.request.EmployeeRegisterRequest;
import API.EventTom.DTO.request.CustomerRegisterRequest;
import API.EventTom.DTO.response.RegisterResponse;
import API.EventTom.exceptions.RuntimeExceptions.EmailAlreadyExistsException;
import API.EventTom.models.*;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.repositories.EmployeeRepository;
import API.EventTom.repositories.UserRepository;
import API.EventTom.services.users.interfaces.IRegistrationService;
import API.EventTom.services.users.interfaces.IRoleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements IRegistrationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserNumberGenerator userNumberGenerator;
    private final IRoleManagementService roleManagementService;

    @Override
    @Transactional
    public ResponseEntity<?> registerCustomer(CustomerRegisterRequest request) {
        validateEmailUniqueness(request.getEmail());

        // Create and save the base user first
        User user = createBaseUser(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword());
        user.setRoles(roleManagementService.getDefaultRoles());
        User savedUser = userRepository.save(user);

        // Create and save the customer profile
        Customer customerProfile = new Customer();
        customerProfile.setUser(savedUser);
        customerProfile.setCustomerNumber(userNumberGenerator.generateCustomerNumber());
        customerRepository.save(customerProfile);

        return ResponseEntity.ok(createRegisterResponse(savedUser));
    }

    @Override
    @Transactional
    public ResponseEntity<?> registerEmployee(EmployeeRegisterRequest request) {
        validateEmailUniqueness(request.getEmail());

        // Create and save the base user first
        User user = createBaseUser(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword());
        user.setRoles(roleManagementService.getRolesByNames(request.getRoles()));
        User savedUser = userRepository.save(user);

        // Create and save the employee profile
        Employee employeeProfile = new Employee();
        employeeProfile.setUser(savedUser);
        employeeProfile.setEmployeeNumber(userNumberGenerator.generateEmployeeNumber());
        employeeRepository.save(employeeProfile);

        return ResponseEntity.ok(createRegisterResponse(savedUser));
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("User already exists with email: " + email);
        }
    }

    private User createBaseUser(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        return user;
    }

    private RegisterResponse createRegisterResponse(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                "User registered successfully"
        );
    }
}