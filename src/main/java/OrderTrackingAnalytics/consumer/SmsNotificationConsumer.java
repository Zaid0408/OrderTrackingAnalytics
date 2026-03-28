package OrderTrackingAnalytics.consumer;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import OrderTrackingAnalytics.model.dto.OrderEventsDTO;
import OrderTrackingAnalytics.model.entity.NotificationLog;
import OrderTrackingAnalytics.service.NotificationService;

@Slf4j
@Component
public class SmsNotificationConsumer {
private final NotificationService notificationService;

    public SmsNotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
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