package API.EventTom.services.interfaces;

import API.EventTom.DTO.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {

    public List<EmployeeDTO> getAllEmployees();
    public EmployeeDTO getEmployeeById(String id);
}
