package API.EventTom.services.users;

import API.EventTom.repositories.RefreshTokenRepository;
import API.EventTom.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionalRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with id: " + userId)
        ));
    }

    @Transactional
    public void deleteByToken(String token) {
        try {
            refreshTokenRepository.deleteByToken(token);
        } catch (OptimisticLockException e) {
            throw new RuntimeException("Refresh token not found");
        }
    }
}
