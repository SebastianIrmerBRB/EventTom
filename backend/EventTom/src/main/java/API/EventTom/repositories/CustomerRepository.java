package API.EventTom.repositories;


import API.EventTom.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
     Optional<Customer> findCustomerByCustomerNumber(String customerNumber);

     Optional<Customer> findByUserId(Long userId);
}
