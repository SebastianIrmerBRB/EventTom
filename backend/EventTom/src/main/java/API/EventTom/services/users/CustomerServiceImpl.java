package API.EventTom.services.users;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Customer;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.services.users.interfaces.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    CustomerRepository customerRepository;
    StandardDTOMapper standardDTOMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        System.out.println(Arrays.toString(customers.toArray()));
        return customers.stream()
                .map(standardDTOMapper::mapCustomerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(String id) {
        Customer customer = customerRepository.findCustomerByCustomerNumber(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return standardDTOMapper.mapCustomerToCustomerDTO(customer);
    }
}
