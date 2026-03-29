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
    private final AnalyticsService analyticsService;
    private final WebSocketService webSocketService;

    public AnalyticsConsumer(AnalyticsService analyticsService, WebSocketService webSocketService){
        this.analyticsService=analyticsService;
        this.webSocketService=webSocketService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_3_NAME)
    public void recieveAnalyticsEvent(OrderEventsDTO orderEventsDTO){
        log.info("Analytics recieved for order: {}",orderEventsDTO.getOrderId());

        // increment event count
        analyticsService.incrementEventCount();

        // get Updated metrics and push to WebSocket
        MetricsDTO metricsDTO = analyticsService.getMetrics();
        
        metricsDTO.setLastUpdated(LocalDateTime.now());
        
        // push to websocket
        webSocketService.pushMetrics(metricsDTO);

        log.info("Analytics updated - Total events: {}", metricsDTO.getTotalEventsProcessed());

    }
}