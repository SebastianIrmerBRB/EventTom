package API.EventTom.services.users.interfaces;

import API.EventTom.DTO.response.TokenRefreshResponse;
import API.EventTom.models.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface IRefreshTokenService {


    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteRefreshToken(String refreshToken, HttpServletResponse response);

    TokenRefreshResponse processRefreshToken(String refreshToken, HttpServletResponse response);
}
