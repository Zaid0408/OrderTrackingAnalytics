package OrderTrackingAnalytics.producer;

import OrderTrackingAnalytics.model.dto.OrderEventsDTO;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
// Method: publishOrderEvent(OrderEventDTO event)
// Uses RabbitTemplate to send message to fanout exchange
@Slf4j
@Service
public class OrderEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public OrderEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void publishOrderEvent(OrderEventsDTO orderEventsDTO) {
        log.info("Publishing order event: {}", orderEventsDTO.getOrderId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_NAME, "", orderEventsDTO);
        log.info("Order event published successfully");
    }


}