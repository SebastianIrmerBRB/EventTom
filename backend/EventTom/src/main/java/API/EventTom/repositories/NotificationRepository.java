package API.EventTom.repositories;

import API.EventTom.models.Notification;
import API.EventTom.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientAndReadOrderByCreatedAtDesc(Person recipient, boolean read);

    List<Notification> findByRecipientOrderByCreatedAtDesc(Person recipient);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.recipient = :recipient AND n.read = false")
    void markAllAsRead(Person recipient);
}
