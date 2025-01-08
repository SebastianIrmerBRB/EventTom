package API.EventTom.config.jwt;

import API.EventTom.models.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${eventtom.app.jwtSecret}")
    private String jwtSecret;

    @Value("${eventtom.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        String email = userPrincipal.getEmail();

        Collection<? extends GrantedAuthority> roles = userPrincipal.getAuthorities();

        List<String> roleNames = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return generateTokenFromEmailAndRoles(email, roleNames); // Changed method name
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())  // changed from setSigningKey to verifyWith
                .build()
                .parseSignedClaims(token)  // changed from parseClaimsJws
                .getPayload()  // changed from getBody
                .getSubject();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(authToken);  // Changed from parse() to parseSignedClaims()
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateTokenFromEmailAndRoles(String email, List<String> roles) {
        List<String> formattedRoles = roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .toList();

        return Jwts.builder()
                .subject(email)
                .claim("roles", formattedRoles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), Jwts.SIG.HS512)  // Explicitly specify HS512
                .compact();
    }
}