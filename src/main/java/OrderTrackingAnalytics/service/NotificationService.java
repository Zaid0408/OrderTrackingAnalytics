package OrderTrackingAnalytics.service;

import OrderTrackingAnalytics.model.entity.NotificationLog;
import OrderTrackingAnalytics.repository.NotificationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationLogRepository notificationLogRepository;

    public void saveNotificationLog(NotificationLog notificationLog) {
        notificationLogRepository.save(notificationLog);
    }

}