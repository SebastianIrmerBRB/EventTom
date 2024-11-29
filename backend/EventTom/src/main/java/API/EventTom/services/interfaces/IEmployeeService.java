package API.EventTom.services.interfaces;

import API.EventTom.DTO.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {

    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(String id);
}
