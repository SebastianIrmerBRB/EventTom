package API.EventTom.services.users.interfaces;

import API.EventTom.DTO.request.CustomerRegisterRequest;
import API.EventTom.DTO.request.EmployeeRegisterRequest;
import org.springframework.http.ResponseEntity;

public interface IRegistrationService {
    ResponseEntity<?> registerCustomer(CustomerRegisterRequest request);
    ResponseEntity<?> registerEmployee(EmployeeRegisterRequest request);
}