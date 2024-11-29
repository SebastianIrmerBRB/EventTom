package API.EventTom.services;

import API.EventTom.models.Customer;
import API.EventTom.models.Employee;
import API.EventTom.models.Person;
import API.EventTom.repositories.CustomerRepository;
import API.EventTom.repositories.EmployeeRepository;
import API.EventTom.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Person findPersonByIdentifier(String identifier) {
        // Mögliche Implementation für WebsiteNotificationSerivce, damit Customer/EmplyeeNumber genutzt wird
        // Aber eigentlich auch nich cool idk
        try {
            Long id = Long.parseLong(identifier);
            return personRepository.findById(id)
                    .orElse(null);
        } catch (NumberFormatException ignored) {
            Customer customer = customerRepository.findCustomerByCustomerNumber(identifier)
                    .orElse(null);
            if (customer != null) {
                return customer;
            }

            Employee employee = employeeRepository.findEmployeeByEmployeeNumber(identifier)
                    .orElse(null);
            if (employee != null) {
                return employee;
            }
        }

        throw new RuntimeException("Person not found with identifier: " + identifier);
    }
}
