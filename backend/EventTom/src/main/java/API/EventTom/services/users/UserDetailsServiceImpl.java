package API.EventTom.services.users;

import API.EventTom.exceptions.RuntimeExceptions.UserNotFoundException;
import API.EventTom.repositories.UserRepository;
import API.EventTom.services.users.interfaces.IUserDetailsService;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements IUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }
}
