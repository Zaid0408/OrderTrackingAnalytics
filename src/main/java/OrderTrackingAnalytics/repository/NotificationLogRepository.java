package OrderTrackingAnalytics.repository;

import OrderTrackingAnalytics.model.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    
}