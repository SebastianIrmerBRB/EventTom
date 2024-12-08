package API.EventTom.services.user.interfaces;

import API.EventTom.models.Role;
import API.EventTom.models.Roles;

import java.util.Set;

public interface IRoleManagementService {
    void assignRole(Long userId, Roles role);
    void assignRoles(Long userId, Set<Roles> roles);
    void removeRole(Long userId, Roles role);
    Set<Role> getDefaultRoles();
    Set<Role> getRolesByNames(Set<Roles> roleNames);
}