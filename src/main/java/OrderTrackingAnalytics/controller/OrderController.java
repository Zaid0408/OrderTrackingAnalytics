package OrderTrackingAnalytics.controller;


import OrderTrackingAnalytics.model.dto.OrderEventsDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<OrderEventsDTO> createOrder(@RequestBody OrderEventsDTO orderEventsDTO) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderEventsDTO.getOrderId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}

