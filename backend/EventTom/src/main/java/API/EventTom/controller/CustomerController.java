package API.EventTom.controller;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.services.interfaces.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")  // Changed to plural form as per REST conventions
@AllArgsConstructor
public class CustomerController {
    ICustomerService customerService;

    // TODO: WRITE DTO FOR EACH REQUEST

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
        return ResponseEntity.ok(customerDTOList);
    }

    @GetMapping("/{id}")  // Path variable for specific customer
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDTO);
    }
}