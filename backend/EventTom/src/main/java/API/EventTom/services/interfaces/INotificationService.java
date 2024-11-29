package API.EventTom.services.interfaces;

import API.EventTom.models.Person;

public interface INotificationService {
    void notifyUser(Person recipient, String message, String notificationType);
}