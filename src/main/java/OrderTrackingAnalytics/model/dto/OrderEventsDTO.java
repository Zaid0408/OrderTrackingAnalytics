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
public class OrderEventsDTO{
    private String orderId;
    private String customerEmail;
    private String customerPhone;
    private Double amount;

    @Builder.Default
    private LocalDateTime timestamp= LocalDateTime.now();

}