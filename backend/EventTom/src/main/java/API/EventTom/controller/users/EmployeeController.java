package API.EventTom.controller.users;

import API.EventTom.DTO.EmployeeDTO;
import API.EventTom.services.users.interfaces.IEmployeeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/employees")
@RequiredArgsConstructor
public class EmployeeController{
    IEmployeeService employeeService;

    // TODO: WRITE DTO FOR EACH REQUEST
    // TODO: CREATE EMPLOYEE / REGISTER

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getCustomerById(@PathVariable String id) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeDTO);
    }

}


