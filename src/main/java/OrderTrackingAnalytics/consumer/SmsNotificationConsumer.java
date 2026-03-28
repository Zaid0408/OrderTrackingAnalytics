package OrderTrackingAnalytics.consumer;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import OrderTrackingAnalytics.model.entity.NotificationLog;
import OrderTrackingAnalytics.service.NotificationService;

@Slf4j
@Component
public class SmsNotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_2_NAME)
    public void recieveSmsNotification(NotificationLog notificationLog)
    {
        log.info("SMS Notification Recieved");
        NotificationService notificationService = new NotificationService();
        notificationService.saveNotificationLog(notificationLog);

    }
}