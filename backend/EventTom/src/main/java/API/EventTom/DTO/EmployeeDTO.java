package API.EventTom.DTO;

import API.EventTom.models.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    public Position position;
    public String name;
    public String email;

}
