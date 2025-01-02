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

        User user = createBaseUser(request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName(), UserType.CUSTOMER);
        user.setRoles(roleManagementService.getDefaultRoles());

        Customer customer = new Customer();
        customer.setUser(user);

        customer.setCustomerNumber(userNumberGenerator.generateCustomerNumber());

        // Save user first
        user = userRepository.save(user);
        customer = customerRepository.save(customer);

        return ResponseEntity.ok(createRegisterResponse(user));
    }

    @Override
    @Transactional
    public ResponseEntity<?> registerEmployee(EmployeeRegisterRequest request) {
        validateEmailUniqueness(request.getEmail());

        // Create and setup the user
        User user = createBaseUser(request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName(), UserType.EMPLOYEE);
        user.setRoles(roleManagementService.getRolesByNames(request.getRoles()));

        // Create and setup the employee
        Employee employee = new Employee();
        employee.setUser(user);
        employee.setEmployeeNumber(userNumberGenerator.generateEmployeeNumber());

        // Save both entities
        user = userRepository.save(user);
        employee = employeeRepository.save(employee);

        return ResponseEntity.ok(createRegisterResponse(user));
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("User already exists with email: " + email);
        }
    }

    private User createBaseUser(String email, String password, String firstname, String lastname, UserType userType) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setUserType(userType);
        user.setFirstName(firstname);
        user.setLastName(lastname);
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