package API.EventTom.services.interfaces;

import API.EventTom.DTO.CustomerDTO;

import java.util.List;

public interface ICustomerService {

    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(String id);

}
