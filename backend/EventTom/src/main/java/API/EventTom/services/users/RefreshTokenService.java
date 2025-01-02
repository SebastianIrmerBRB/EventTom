package API.EventTom.services.users;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import API.EventTom.DTO.response.TokenRefreshResponse;
import API.EventTom.config.jwt.JwtUtils;
import API.EventTom.exceptions.RuntimeExceptions.UserNotFoundException;
import API.EventTom.exceptions.tokenException.TokenRefreshException;
import API.EventTom.models.RefreshToken;
import API.EventTom.models.User;
import API.EventTom.models.UserDetailsImpl;
import API.EventTom.repositories.RefreshTokenRepository;
import API.EventTom.repositories.UserRepository;
import API.EventTom.services.users.interfaces.IRefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${eventtom.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final TransactionalRefreshTokenService transactionalService;
    private final JwtUtils jwtUtils;
    private final TokenCookieService tokenCookieService;


    @Scheduled(cron = "0 0 18 * * ?", zone = "Europe/Berlin")
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        RefreshToken refreshToken = buildRefreshToken(user);

        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken buildRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
    @Transactional
    public void deleteRefreshToken(String refreshToken, HttpServletResponse response) {
        response.addHeader("Set-Cookie", tokenCookieService.createClearJwtCookie().toString());
        response.addHeader("Set-Cookie", tokenCookieService.createClearRefreshTokenCookie().toString());

        if (refreshToken != null && !refreshToken.isEmpty()) {
            transactionalService.deleteByToken(refreshToken);
        }
    }

    @Transactional
    public TokenRefreshResponse processRefreshToken(String refreshToken, HttpServletResponse response) {

        RefreshToken refreshTokenObj = findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));

        refreshTokenObj = verifyExpiration(refreshTokenObj);
        User user = refreshTokenObj.getUser();

        RefreshToken newRefreshToken = updateRefreshToken(refreshTokenObj);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        List<String> roleNames = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        String newJwt = jwtUtils.generateTokenFromEmailAndRoles(user.getEmail(), roleNames);

        return new TokenRefreshResponse(newJwt, newRefreshToken.getToken());
    }

    @Transactional
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

}