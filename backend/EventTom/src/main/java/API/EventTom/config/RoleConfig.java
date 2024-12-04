package API.EventTom.config;

import API.EventTom.models.Role;
import API.EventTom.models.Roles;
import API.EventTom.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class RoleConfig {

    private final RoleRepository roleRepository;
    @Bean
    CommandLineRunner initRoles() {
        return args -> {
            if (roleRepository.count() == 0) {
                Arrays.stream(Roles.values())
                        .forEach(role -> roleRepository.save(Role.from(role)));
            }
        };
    }
}