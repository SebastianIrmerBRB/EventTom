package API.EventTom.DTO;

import API.EventTom.models.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    public Set<Roles> roles;
    public String name;
    public String email;

}
