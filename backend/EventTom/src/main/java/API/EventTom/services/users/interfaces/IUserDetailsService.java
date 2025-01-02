package API.EventTom.services.users.interfaces;

import API.EventTom.exceptions.RuntimeExceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetailsService {
    UserDetails loadUserByEmail(String email) throws UserNotFoundException;
}
