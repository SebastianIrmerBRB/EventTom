package API.EventTom.services.users.interfaces;

import API.EventTom.DTO.request.LoginRequest;
import API.EventTom.DTO.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthenticationService {
    LoginResponse authenticateUser(LoginRequest request, HttpServletResponse response);
    ResponseEntity<?> logout();
}