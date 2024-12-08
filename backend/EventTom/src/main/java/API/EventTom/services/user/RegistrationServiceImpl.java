package API.EventTom.services.user;

import API.EventTom.DTO.request.EmployeeRegisterRequest;
import API.EventTom.DTO.request.CustomerRegisterRequest;
import API.EventTom.DTO.response.RegisterResponse;
import API.EventTom.exceptions.RuntimeExceptions.EmailAlreadyExistsException;
import API.EventTom.models.*;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.repositories.EmployeeRepository;
import API.EventTom.repositories.UserRepository;
import API.EventTom.services.user.interfaces.IRegistrationService;
import API.EventTom.services.user.interfaces.IRoleManagementService;
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

        Customer customer = createCustomer(request);
        customer.setRoles(roleManagementService.getDefaultRoles());
        User savedUser = customerRepository.save(customer);

        return ResponseEntity.ok(createRegisterResponse(savedUser));
    }

    @Override
    @Transactional
    public ResponseEntity<?> registerEmployee(EmployeeRegisterRequest request) {
        validateEmailUniqueness(request.getEmail());

        Employee employee = createEmployee(request);
        employee.setRoles(roleManagementService.getRolesByNames(request.getRoles()));
        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntity.ok(createRegisterResponse(savedEmployee));
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("User already exists with email: " + email);
        }
    }

    private Customer createCustomer(CustomerRegisterRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setCustomerNumber(userNumberGenerator.generateCustomerNumber());
        return customer;
    }

    private Employee createEmployee(EmployeeRegisterRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setEmployeeNumber(userNumberGenerator.generateEmployeeNumber());
        return employee;
    }

    private RegisterResponse createRegisterResponse(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                "User registered successfully"
        );
    }
}