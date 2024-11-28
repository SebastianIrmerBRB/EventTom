package API.EventTom.repositories;


import API.EventTom.DTO.CustomerDTO;
import API.EventTom.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Customer findAllById(long id);
}
