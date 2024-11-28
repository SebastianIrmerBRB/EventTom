package API.EventTom.services.interfaces;

import API.EventTom.DTO.CustomerDTO;

import java.util.List;

public interface ICustomerService {

    public List<CustomerDTO> getAllCustomers();
    public CustomerDTO getCustomerById(int id);

}
