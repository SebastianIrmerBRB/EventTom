package API.EventTom.services.users;

import API.EventTom.exceptions.RuntimeExceptions.RoleNotFoundException;
import API.EventTom.exceptions.RuntimeExceptions.UserNotFoundException;
import API.EventTom.models.*;
import API.EventTom.repositories.RoleRepository;
import API.EventTom.repositories.UserRepository;
import API.EventTom.services.users.interfaces.IRoleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class RoleManagementServiceImpl implements IRoleManagementService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void assignRole(Long userId, Roles role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role roleEntity = getRoleByName(role);
        user.getRoles().add(roleEntity);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, Set<Roles> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Set<Role> roleEntities = getRolesByNames(roles);
        user.setRoles(roleEntities);
        userRepository.save(user);
    }

    @Override
    public void removeRole(Long userId, Roles role) {

    }

    @Override
    public Set<Role> getDefaultRoles() {
        Role defaultRole = getRoleByName(Roles.NONE);
        return Set.of(defaultRole);
    }

    @Override
    public Set<Role> getRolesByNames(Set<Roles> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return getDefaultRoles();
        }

        return roleNames.stream()
                .map(this::getRoleByName)
                .collect(Collectors.toSet());
    }

    private Role getRoleByName(Roles roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));
    }
}