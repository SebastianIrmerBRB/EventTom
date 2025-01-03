package API.EventTom.controller;

import API.EventTom.models.Notification;
import API.EventTom.services.notifications.WebsiteNotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final WebsiteNotificationServiceImpl notificationService;
    // No Auth Principal --> Prototyping
    @GetMapping("/unread/{personId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long personId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByPersonId(personId));
    }
    // No Auth Principal --> Prototyping
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Notification>> getAllNotifications(@PathVariable Long personId) {
        return ResponseEntity.ok(notificationService.getAllNotificationsByPersonId(personId));
    }

    // No Auth Principal --> Prototyping
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-all-read/{personId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long personId) {
        notificationService.markAllAsReadByPersonId(personId);
        return ResponseEntity.ok().build();
    }

    // Additional endpoint for testing/prototyping
    @GetMapping("/test/{personId}")
    public ResponseEntity<Void> createTestNotification(@PathVariable Long personId) {
        notificationService.createTestNotification(personId);
        return ResponseEntity.ok().build();
    }
}