package OrderTrackingAnalytics.controller;


import OrderTrackingAnalytics.model.dto.OrderEventsDTO;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;

import OrderTrackingAnalytics.producer.OrderEventProducer;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderEventProducer orderEventProducer;

    public OrderController(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;

    }
    @PostMapping
    public ResponseEntity<OrderEventsDTO> createOrder(@RequestBody OrderEventsDTO orderEventsDTO) {
        log.info("Recieved Request for Order : {}", orderEventsDTO.getOrderId());

        //publsih event to RabbitMQnusng the producer to directly push request body to the queue
        orderEventProducer.publishOrderEvent(orderEventsDTO);

        log.info("Order created successfully: {}", orderEventsDTO.getOrderId());

        // return 201 response for order created 
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(orderEventsDTO);
    }
}

