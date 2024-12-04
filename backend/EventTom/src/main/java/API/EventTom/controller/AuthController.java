package API.EventTom.controller;



import API.EventTom.DTO.request.EmployeeRegisterRequest;
import API.EventTom.DTO.request.LoginRequest;
import API.EventTom.DTO.request.CustomerRegisterRequest;
import API.EventTom.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeRegisterRequest request) {
        return authService.registerEmployee(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}