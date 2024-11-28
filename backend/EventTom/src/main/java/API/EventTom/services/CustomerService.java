package API.EventTom.services;

import API.EventTom.DTO.CustomerDTO;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Customer;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.services.interfaces.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    CustomerRepository customerRepository;
    StandardDTOMapper standardDTOMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
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
