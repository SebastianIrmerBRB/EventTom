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
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserNumberGenerator userNumberGenerator;
    private final IRoleManagementService roleManagementService;

    @Override
    @Transactional
    public ResponseEntity<?> registerCustomer(CustomerRegisterRequest request) {
        validateEmailUniqueness(request.getEmail());

        // Create the user with its profile
        User user = createBaseUser(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), UserType.CUSTOMER);
        user.setRoles(roleManagementService.getDefaultRoles());

        Customer customerProfile = (Customer) user.getUserProfile();
        customerProfile.setCustomerNumber(userNumberGenerator.generateCustomerNumber());

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(createRegisterResponse(savedUser));
    }

    @Override
    @Transactional
    public ResponseEntity<?> registerEmployee(EmployeeRegisterRequest request) {
        validateEmailUniqueness(request.getEmail());

        User user = createBaseUser(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), UserType.EMPLOYEE);
        user.setRoles(roleManagementService.getRolesByNames(request.getRoles()));

        Employee employeeProfile = (Employee) user.getUserProfile();
        employeeProfile.setEmployeeNumber(userNumberGenerator.generateEmployeeNumber());

        // Save the user - cascade will save the profile
        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(createRegisterResponse(savedUser));
    }


    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("User already exists with email: " + email);
        }
    }

    private User createBaseUser(String firstName, String lastName, String email, String password, UserType userType) {
        // Create base user with auth details
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setUserType(userType);

        UserProfile profile;
        if (userType == UserType.CUSTOMER) {
            profile = new Customer();
        } else {
            profile = new Employee();
        }

        profile.setUser(user);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        user.setUserProfile(profile);

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