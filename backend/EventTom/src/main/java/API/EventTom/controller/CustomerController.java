package API.EventTom.controller;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.services.interfaces.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerController {

    ICustomerService customerService;

    // TODO: WRITE DTO FOR EACH REQUEST

    public ResponseEntity<List<CustomerDTO>> getCustomerService() {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
        return ResponseEntity.ok(customerDTOList) ;
    }

}
