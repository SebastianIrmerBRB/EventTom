package API.EventTom.observers;

import API.EventTom.models.Roles;
import API.EventTom.models.User;
import API.EventTom.repositories.EmployeeRepository;
import API.EventTom.services.notifications.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketNotificationListener {
    private final EmployeeRepository employeeRepository;
    private final INotificationService notificationService;

    @Async
    @EventListener
    public void handleTicketPurchase(TicketPurchaseEvent event) {
        sendCustomerNotification(event, "TICKET_PURCHASE");

        if (shouldNotifyEventManager(event)) {
            sendManagerNotification(event, "TICKET_PURCHASE");
        }
    }

    private void sendCustomerNotification(TicketPurchaseEvent event, String notificationType) {
        String message = createCustomerMessage(event);
        User recipient = event.getTicket().getCustomer().getUser();
        notificationService.notifyUser(recipient, message, notificationType);
    }

    private void sendManagerNotification(TicketPurchaseEvent event, String notificationType) {
        String message = createManagerMessage(event);
        employeeRepository.findByRoleAndEvent(Roles.EVENT_MANAGER, event.getEvent())
                .forEach(manager -> {
                    User userManager = manager.getUser();
                    notificationService.notifyUser(userManager, message, notificationType);
                });
    }

    private boolean shouldNotifyEventManager(TicketPurchaseEvent event) {
        return event.getEvent().isThresholdReached() ||
                event.getSoldPercentage() >= 90.0 ||
                event.getRemainingTickets() <= 10;
    }

    private String createManagerMessage(TicketPurchaseEvent event) {
        return String.format(
                "Event Update: %s\n" +
                        "Tickets Sold: %d/%d (%.1f%%)\n" +
                        "Remaining Tickets: %d\n" +
                        "Latest Purchase: %s\n" +
                        "Purchase Time: %s",
                event.getEvent().getTitle(),
                event.getEvent().getTotalSoldTickets(),
                event.getEvent().getMaxTotalTickets(),
                event.getSoldPercentage(),
                event.getRemainingTickets(),
                event.getTicket().getCustomer().toString(),
                event.getTicket().getPurchaseDate()
        );
    }

    private String createCustomerMessage(TicketPurchaseEvent event) {
        return String.format(
                "Thank you for your purchase!\n\n" +
                        "Event: %s\n" +
                        "Date: %s\n" +
                        "Purchase Time: %s\n\n" +
                        "Please keep this confirmation for your records.",
                event.getEvent().getTitle(),
                event.getEvent().getDateOfEvent(),
                event.getTicket().getPurchaseDate()
        );
    }
}