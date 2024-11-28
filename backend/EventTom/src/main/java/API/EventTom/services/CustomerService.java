package API.EventTom.services;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.services.interfaces.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return List.of();
    }

    @Override
    public CustomerDTO getCustomerById(int id) {
        return null;
    }
}
