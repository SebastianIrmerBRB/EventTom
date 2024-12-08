package API.EventTom.services.user.interfaces;

import API.EventTom.DTO.request.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthenticationService {
    ResponseEntity<?> login(LoginRequest request);
    ResponseEntity<?> logout();
}