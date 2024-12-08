package API.EventTom.services.users;

import API.EventTom.DTO.EmployeeDTO;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Employee;
import API.EventTom.repositories.EmployeeRepository;
import API.EventTom.services.users.interfaces.IEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    EmployeeRepository employeeRepository;
    StandardDTOMapper standardDTOMapper;
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(standardDTOMapper::mapEmployeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findEmployeeByEmployeeNumber(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return standardDTOMapper.mapEmployeeToEmployeeDTO(employee);
    }
}


