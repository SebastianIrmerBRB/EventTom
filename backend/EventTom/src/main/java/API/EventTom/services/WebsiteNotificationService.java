package API.EventTom.services;

import API.EventTom.models.Notification;
import API.EventTom.models.Person;
import API.EventTom.repositories.NotificationRepository;
import API.EventTom.repositories.PersonRepository;
import API.EventTom.services.interfaces.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebsiteNotificationService implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final PersonRepository personRepository;

    @Override
    @Transactional
    public void notifyUser(Person recipient, String message, String notificationType) {
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

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        return notificationRepository.findByRecipientAndReadOrderByCreatedAtDesc(person, false);
    }

    @Transactional(readOnly = true)
    public List<Notification> getAllNotificationsByPersonId(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(person);
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
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        notificationRepository.markAllAsRead(person);
    }

    // Method for testing/prototyping
    @Transactional
    public void createTestNotification(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Notification notification = new Notification();
        notification.setRecipient(person);
        notification.setMessage("Test notification at " + LocalDateTime.now());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setNotificationType("TEST");

        notificationRepository.save(notification);
    }
}