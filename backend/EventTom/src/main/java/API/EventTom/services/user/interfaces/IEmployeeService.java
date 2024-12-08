package API.EventTom.services.user.interfaces;

import API.EventTom.DTO.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {

    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(String id);
}
