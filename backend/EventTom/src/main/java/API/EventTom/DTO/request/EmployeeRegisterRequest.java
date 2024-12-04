package API.EventTom.DTO.request;

import API.EventTom.models.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class EmployeeRegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Roles> roles;
}
