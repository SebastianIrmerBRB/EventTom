package API.EventTom.services;

import API.EventTom.DTO.EmployeeDTO;
import API.EventTom.services.interfaces.IEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService implements IEmployeeService {
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return List.of();
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        return null;
    }
}


