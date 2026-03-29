package OrderTrackingAnalytics.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.*;

/*
Concept:

@Bean = Creates objects managed by Spring
Fanout Exchange type= Message router that sends messages to multiple queues
Fanout: Broadcasts copies of the message to all queues bound to it
Queue = Message storage
Binding = Connects queue to exchange
*/
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_1_NAME = "queue.fanout.email";
    public static final String QUEUE_2_NAME = "queue.fanout.sms";
    public static final String QUEUE_3_NAME = "queue.fanout.analytics";
    public static final String FANOUT_NAME = "fanout.exchange";


    @Bean
    public Queue queue1() {
        return new Queue(QUEUE_1_NAME);
    }
    @Bean
    public Queue queue2() {
        return new Queue(QUEUE_2_NAME);
    }
    @Bean
    public Queue queue3() {
        return new Queue(QUEUE_3_NAME);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_NAME);
    }

    @Bean
    public Binding binding1(Queue queue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }
    @Bean
    public Binding binding2(Queue queue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }
    @Bean
    public Binding binding3(Queue queue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue3).to(fanoutExchange);
    }

    /**
     * JSON message converter for serializing/deserializing DTOs
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}