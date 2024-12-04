package API.EventTom.services.interfaces;

import API.EventTom.models.User;

public interface IPersonService {
    User findPersonByIdentifier(String identifier);
}
