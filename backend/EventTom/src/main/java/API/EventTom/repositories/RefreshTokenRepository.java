package API.EventTom.repositories;
import API.EventTom.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import API.EventTom.models.User;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByUser(User user);

    @Modifying
    void deleteByToken(String token);

    @Modifying
    @Query("DELETE FROM refreshtoken t WHERE t.expiryDate <= :currentInstant")
    void deleteAllExpiredTokens(Instant currentInstant);

}