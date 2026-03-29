package OrderTrackingAnalytics.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import OrderTrackingAnalytics.model.dto.MetricsDTO;
import OrderTrackingAnalytics.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/metrics")
public class MetricsController{
    private final AnalyticsService analyticsService;

    public MetricsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<MetricsDTO> getMetrics(){
        log.info("Getting metrics");
        MetricsDTO metrics= analyticsService.getMetrics();
        return ResponseEntity.ok(metrics);
    }
}


