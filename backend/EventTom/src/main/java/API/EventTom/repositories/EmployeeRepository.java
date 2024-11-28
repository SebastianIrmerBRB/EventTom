package API.EventTom.repositories;

import API.EventTom.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Employee findById(long id);
}
