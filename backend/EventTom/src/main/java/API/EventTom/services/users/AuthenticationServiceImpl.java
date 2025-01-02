package API.EventTom.services.users;
import API.EventTom.DTO.request.LoginRequest;
import API.EventTom.DTO.response.LoginResponse;
import API.EventTom.config.jwt.JwtUtils;
import API.EventTom.exceptions.RuntimeExceptions.UserNotFoundException;
import API.EventTom.models.*;
import API.EventTom.repositories.UserRepository;
import API.EventTom.services.users.interfaces.IAuthenticationService;
import API.EventTom.services.users.interfaces.IRefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IRefreshTokenService refreshTokenService;
    private final TokenCookieService tokenCookieService;

    public LoginResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String jwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        System.out.println(jwt);
        System.out.println(refreshToken);

        response.addHeader("Set-Cookie", tokenCookieService.createJwtCookie(jwt).toString());
        response.addHeader("Set-Cookie", tokenCookieService.createRefreshTokenCookie(refreshToken.getToken()).toString());


        return new LoginResponse(userDetails.getId(),
                userDetails.getUsername(), roles);
    }

    @Override
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("Logged out successfully");
    }
}