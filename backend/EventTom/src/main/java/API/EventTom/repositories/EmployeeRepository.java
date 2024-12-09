package API.EventTom.repositories;

import API.EventTom.models.Employee;
import API.EventTom.models.Event;
import API.EventTom.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByEmployeeNumber(String number);

    @Query("SELECT e FROM Employee e JOIN e.user.roles r WHERE r.name = :role AND :event MEMBER OF e.managedEvents")
    List<Employee> findByRoleAndEvent(@Param("role") Roles role, @Param("event") Event event);
}
