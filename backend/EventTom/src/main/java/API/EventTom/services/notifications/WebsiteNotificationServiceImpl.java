package API.EventTom.services.notifications;

import API.EventTom.models.Event;
import API.EventTom.models.Notification;
import API.EventTom.models.User;
import API.EventTom.repositories.NotificationRepository;
import API.EventTom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebsiteNotificationServiceImpl implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void notifyUser(User recipient, String message, String notificationType) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setNotificationType(notificationType);

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotificationsByPersonId(Long personId) {
        // TODO: vllt. Nutzen von CustomerNumber / EmployeeNumber
        //  indem da erst Anfrage gemacht wird und dann
        //  dadurch die Anfrage (aber eig. nich gut, weil für jeden Nutzertyp einzelne Methode)

        User user = userRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        return notificationRepository.findByRecipientAndIsReadOrderByCreatedAtDesc(user, false);
    }

    @Transactional(readOnly = true)
    public List<Notification> getAllNotificationsByPersonId(Long personId) {
        User user = userRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void markAllAsReadByPersonId(Long personId) {
        User user = userRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        notificationRepository.markAllAsRead(user);
    }

    // Method for testing/prototyping
    @Transactional
    public void createTestNotification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Notification notification = new Notification();
        notification.setRecipient(user);
        notification.setMessage("Test notification at " + LocalDateTime.now());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setNotificationType("TEST");

        notificationRepository.save(notification);
    }



}