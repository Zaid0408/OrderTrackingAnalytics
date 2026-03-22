package OrderTrackingAnalytics.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricsDTO{
    private Long totalEventsProcessed;
    private Long totalEmailsSent;
    private Long totalSmsSent;

    @Builder.Default
    private LocalDateTime lastUpdated= LocalDateTime.now();
}