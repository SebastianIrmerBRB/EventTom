package API.EventTom.repositories;

import API.EventTom.models.Employee;
import API.EventTom.models.Event;
import API.EventTom.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByEmployeeNumber(String number);
    @Query("SELECT e FROM Employee e WHERE e.position = :position AND :event MEMBER OF e.managedEvents")
    List<Employee> findByPositionAndEvent(@Param("position") Position position, @Param("event") Event event);

}
