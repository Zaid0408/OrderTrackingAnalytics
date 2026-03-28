package OrderTrackingAnalytics.consumer;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import OrderTrackingAnalytics.model.entity.NotificationLog;
import OrderTrackingAnalytics.service.NotificationService;

@Slf4j
@Component
public class EmailNotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_1_NAME)
    public void recieveEmailNotification(NotificationLog notificationLog)
    {
        log.info("Email Notification Recieved");
        NotificationService notificationService = new NotificationService();
        notificationService.saveNotificationLog(notificationLog);

    }
}