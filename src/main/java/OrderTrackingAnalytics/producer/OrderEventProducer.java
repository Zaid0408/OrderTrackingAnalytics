package OrderTrackingAnalytics.producer;

import OrderTrackingAnalytics.model.dto.OrderEventsDTO;
import OrderTrackingAnalytics.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public OrderEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void publishOrderEvent(OrderEventsDTO orderEventsDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_NAME, "", orderEventsDTO);
    }


}