package OrderTrackingAnalytics.consumer;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import OrderTrackingAnalytics.model.dto.OrderEventsDTO;
import OrderTrackingAnalytics.model.entity.NotificationLog;
import OrderTrackingAnalytics.service.NotificationService;
import OrderTrackingAnalytics.service.AnalyticsService;
import java.time.LocalDateTime;

@Slf4j
@Component
public class EmailNotificationConsumer {

    private final NotificationService notificationService;
    private final AnalyticsService analyticsService;

    public EmailNotificationConsumer(NotificationService notificationService, AnalyticsService analyticsService) {
        this.notificationService = notificationService;
        this.analyticsService = analyticsService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_1_NAME)
    public void recieveEmailNotification(OrderEventsDTO orderEventsDTO)
    {
        log.info("Email Notification Recieved for Order: {}",orderEventsDTO.getOrderId());
        // simulate sending email notification

        try {
            Thread.sleep(200);
            log.info("Email Sent to {} for Order: {}",orderEventsDTO.getCustomerEmail(),orderEventsDTO.getOrderId());

            // save to h2 database
            notificationService.saveNotificationLog(
                orderEventsDTO.getOrderId(),
                NotificationLog.NotificationType.EMAIL,
                "SUCCESS"
            );
            analyticsService.incrementEmailCount();
            log.info("Notification Log Saved for Order: {}", orderEventsDTO.getOrderId());

        } catch (Exception e) {
            log.error("Error sending email notification for Order: {}", orderEventsDTO.getOrderId(), e);

            notificationService.saveNotificationLog(
                orderEventsDTO.getOrderId(),
                NotificationLog.NotificationType.EMAIL,
                "FAILED"
            );
        }
    }
}