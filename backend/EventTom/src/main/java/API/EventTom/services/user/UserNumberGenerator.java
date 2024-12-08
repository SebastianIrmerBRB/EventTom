package API.EventTom.services.user;


import org.springframework.stereotype.Component;

// future: (Strategy Pattern)
@Component
public class UserNumberGenerator {
    public String generateCustomerNumber() {
        return "C" + System.currentTimeMillis();
    }

    public String generateEmployeeNumber() {
        return "E" + System.currentTimeMillis();
    }
}
