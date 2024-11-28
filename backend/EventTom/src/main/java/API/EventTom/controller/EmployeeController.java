package API.EventTom.controller;

import API.EventTom.DTO.EmployeeDTO;
import API.EventTom.services.interfaces.IEmployeeService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeController{
    IEmployeeService employeeService;

    // TODO: WRITE DTO FOR EACH REQUEST

    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

}


