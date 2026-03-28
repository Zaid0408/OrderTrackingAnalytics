package OrderTrackingAnalytics.consumer;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsConsumer {
    public static AtomicInteger count = new AtomicInteger(0);

    public static AtomicInteger getCount() {
        return count;
    }

    public void incrementEmailCount()
    {
        
    }
}