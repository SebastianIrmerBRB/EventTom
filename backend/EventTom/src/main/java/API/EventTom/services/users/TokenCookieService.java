package API.EventTom.services.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class TokenCookieService {

    @Value("${eventtom.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${eventtom.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    public ResponseCookie createJwtCookie(String jwt) {
        return ResponseCookie.from("JWT_TOKEN", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtExpirationMs) // Sets the max age of the cookie to jwtExpirationMs
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String refreshtoken) {
        return ResponseCookie.from("REFRESH_TOKEN", refreshtoken)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtRefreshExpirationMs) // Sets the max age of the cookie to jwtRefreshExpirationMs
                .build();
    }

    public ResponseCookie createClearJwtCookie() {
        return ResponseCookie.from("JWT_TOKEN", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0) // Sets the max age to 0 to clear the cookie
                .build();
    }

    public ResponseCookie createClearRefreshTokenCookie() {
        return ResponseCookie.from("REFRESH_TOKEN", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0) // Sets the max age to 0 to clear the cookie
                .build();
    }
}
