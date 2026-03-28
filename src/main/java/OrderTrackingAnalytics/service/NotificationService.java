package OrderTrackingAnalytics.service;

import OrderTrackingAnalytics.model.entity.NotificationLog;
import OrderTrackingAnalytics.repository.NotificationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
    @Autowired
    private final NotificationLogRepository notificationLogRepository;

    public NotificationService(NotificationLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    public void saveNotificationLog(String orderId, NotificationLog.NotificationType notificationType, String status) {
        NotificationLog notificationLog=NotificationLog.builder()
            .orderId(orderId)
            .notificationType(notificationType)
            .status(status)
            .sentAt(LocalDateTime.now())
            .build();

            notificationLogRepository.save(notificationLog);
            log.info("Notification log saved: {} - {} - {}", orderId, notificationType, status);
    }

}