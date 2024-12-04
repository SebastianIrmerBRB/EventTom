package API.EventTom.DTO.request;

import API.EventTom.models.Roles;
import lombok.Data;

@Data
public class CustomerRegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
