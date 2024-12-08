package API.EventTom.services.notification;

import API.EventTom.models.User;

public interface INotificationService {
    void notifyUser(User recipient, String message, String notificationType);
}