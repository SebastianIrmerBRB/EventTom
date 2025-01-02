package API.EventTom.controller.users;

import API.EventTom.DTO.request.LoginRequest;
import API.EventTom.DTO.response.LoginResponse;
import API.EventTom.DTO.response.MessageResponseDTO;
import API.EventTom.DTO.response.TokenRefreshResponse;
import API.EventTom.services.users.TokenCookieService;
import API.EventTom.services.users.interfaces.IAuthenticationService;
import API.EventTom.services.users.interfaces.IRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final IRefreshTokenService refreshTokenService;
    private final TokenCookieService tokenCookieService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authenticationService.authenticateUser(request, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponseDTO> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromRequest(request);
        refreshTokenService.deleteRefreshToken(refreshToken, response);
        return ResponseEntity.ok(new MessageResponseDTO("You've been signed out!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshJwtToken(HttpServletRequest request, HttpServletResponse response) {
        try{
            String refreshToken = extractRefreshTokenFromRequest(request);
            TokenRefreshResponse tokenRefresh = refreshTokenService.processRefreshToken(refreshToken, response);

            response.addHeader("Set-Cookie", tokenCookieService.createJwtCookie(tokenRefresh.getAccessToken()).toString());
            response.addHeader("Set-Cookie", tokenCookieService.createRefreshTokenCookie(tokenRefresh.getRefreshToken()).toString());

            return ResponseEntity.ok(tokenRefresh);
        } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException e) {
            throw new RuntimeException("Cannot set user authentication: " + e.getMessage());
        }
    }

    private String extractRefreshTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "REFRESH_TOKEN".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @GetMapping("authenticated")
    public ResponseEntity<Boolean> isAuthenticated() {
        return ResponseEntity.ok(true);
    }

}