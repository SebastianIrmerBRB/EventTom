package API.EventTom.services.interfaces;

import API.EventTom.models.Person;

public interface IPersonService {
    Person findPersonByIdentifier(String identifier);
}
