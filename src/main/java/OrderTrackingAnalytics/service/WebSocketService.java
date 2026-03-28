package OrderTrackingAnalytics.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import OrderTrackingAnalytics.model.dto.MetricsDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void pushMetrics(MetricsDTO metricsDTO) {
        log.debug("Pushing metrics update to WebSocket");
        simpMessagingTemplate.convertAndSend("/topic/metrics", metricsDTO);
    }

}