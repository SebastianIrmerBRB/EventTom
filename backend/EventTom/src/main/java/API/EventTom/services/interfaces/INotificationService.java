package API.EventTom.services.interfaces;

import API.EventTom.models.User;

public interface INotificationService {
    void notifyUser(User recipient, String message, String notificationType);
}