package API.EventTom.controller;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.DTO.EmployeeDTO;
import API.EventTom.DTO.EventDTO;
import API.EventTom.services.interfaces.IEmployeeService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/employees")
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


