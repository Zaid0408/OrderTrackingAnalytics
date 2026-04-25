package OrderTrackingAnalytics.service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import OrderTrackingAnalytics.model.dto.MetricsDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnalyticsService {
    private final AtomicLong totalEmailSent = new AtomicLong(0);
    private final AtomicLong totalSmsSent = new AtomicLong(0);
    private final AtomicLong totalEventsProcessed = new AtomicLong(0);

    public void incrementEmailCount()
    {
        long count = totalEmailSent.incrementAndGet();
        log.debug("Email sent count: {}", count);
    }

    public void incrementSmsCount()
    {
        long count = totalSmsSent.incrementAndGet();
        log.debug("Sms sent count: {}", count);
    }

    public void incrementEventCount() {
        long count = totalEventsProcessed.incrementAndGet();
        log.debug("Event count incremented to: {}", count);
    }

    public MetricsDTO getMetrics() {
        return MetricsDTO.builder()
            .totalEmailsSent(totalEmailSent.get())
            .totalSmsSent(totalSmsSent.get())
            .totalEventsProcessed(totalEventsProcessed.get())
            .lastUpdated(LocalDateTime.now())
            .build();
    }
}