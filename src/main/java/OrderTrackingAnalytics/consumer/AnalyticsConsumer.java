package OrderTrackingAnalytics.consumer;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import OrderTrackingAnalytics.model.dto.MetricsDTO;
import OrderTrackingAnalytics.service.AnalyticsService;
import OrderTrackingAnalytics.service.WebSocketService;
import OrderTrackingAnalytics.model.dto.OrderEventsDTO;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AnalyticsConsumer {
    
}