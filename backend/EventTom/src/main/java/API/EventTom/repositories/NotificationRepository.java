package API.EventTom.repositories;

import API.EventTom.models.Notification;
import API.EventTom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Change from 'Read' to 'IsRead' in the method name
    List<Notification> findByRecipientAndIsReadOrderByCreatedAtDesc(User recipient, boolean isRead);

    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

    // Assuming you have a @Query for markAllAsRead, it needs to be updated too
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient = :person")
    void markAllAsRead(@Param("person") User user);
}