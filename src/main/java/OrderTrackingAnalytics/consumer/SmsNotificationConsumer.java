package OrderTrackingAnalytics.consumer;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import OrderTrackingAnalytics.model.dto.OrderEventsDTO;
import OrderTrackingAnalytics.model.entity.NotificationLog;
import OrderTrackingAnalytics.service.NotificationService;
import OrderTrackingAnalytics.service.AnalyticsService;

@Slf4j
@Component
public class SmsNotificationConsumer {
    private final NotificationService notificationService;
    private final AnalyticsService analyticsService;

    public SmsNotificationConsumer(NotificationService notificationService, AnalyticsService analyticsService) {
        this.notificationService = notificationService;
        this.analyticsService = analyticsService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_1_NAME)
    public void recieveSmsNotification(OrderEventsDTO orderEventsDTO)
    {
        log.info("SMS Notification Recieved for Order: {}",orderEventsDTO.getOrderId());
        // simulate sending Sms notification

        try {
            Thread.sleep(200);
            log.info("Sms Sent to {} for Order: {}",orderEventsDTO.getCustomerPhone(),orderEventsDTO.getOrderId());

            // save to h2 database
            notificationService.saveNotificationLog(
                orderEventsDTO.getOrderId(),
                NotificationLog.NotificationType.SMS,
                "SUCCESS"
            );
            analyticsService.incrementSmsCount();
            log.info("Notification Log Saved for Order: {}", orderEventsDTO.getOrderId());

        } catch (Exception e) {
            log.error("Error sending Sms notification for Order: {}", orderEventsDTO.getOrderId(), e);

            notificationService.saveNotificationLog(
                orderEventsDTO.getOrderId(),
                NotificationLog.NotificationType.SMS,
                "FAILED"
            );
        }
    }
}