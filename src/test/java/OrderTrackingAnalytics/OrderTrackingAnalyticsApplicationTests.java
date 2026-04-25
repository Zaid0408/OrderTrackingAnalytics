package OrderTrackingAnalytics;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import OrderTrackingAnalytics.model.dto.OrderEventsDTO;
import OrderTrackingAnalytics.model.dto.MetricsDTO;
import OrderTrackingAnalytics.service.AnalyticsService;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderTrackingAnalyticsApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private AnalyticsService analyticsService;
	@Test
	void contextLoads() { // Tests that Spring context loads successfully
	}

	@Test
	void testCreateOrderSuccess()
	{
		OrderEventsDTO order= OrderEventsDTO.builder()
			.orderId("TEST-ORD-001")
			.customerEmail("test23@example.com")
			.customerPhone("+1234567890")
			.amount(789.0)
			.build();

		ResponseEntity<OrderEventsDTO> res= restTemplate.postForEntity(
			"/api/orders", 
			order, 
			OrderEventsDTO.class
		);

		// Assert that the response status is 201 (CREATED)
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(res.getBody()).isNotNull();
		assertThat(res.getBody().getOrderId()).isEqualTo(order.getOrderId());
	}

	@Test
	void testGetMetrics()
	{
		ResponseEntity<MetricsDTO> res= restTemplate.getForEntity(
			"/api/metrics", 
			MetricsDTO.class
		);		

		// Assert that the response status is 200 (OK)
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(res.getBody()).isNotNull();
		assertThat(res.getBody().getTotalEventsProcessed()).isNotNull();
		assertThat(res.getBody().getTotalEmailsSent()).isNotNull();
		assertThat(res.getBody().getTotalSmsSent()).isNotNull();
	}

	@Test
	void testMultipleOrders_MetricsIncrement() throws InterruptedException {
		// Arrange: Get initial metrics
		MetricsDTO initialMetrics = restTemplate.getForObject(
			"/api/metrics",
			MetricsDTO.class
		);
		
		// Act: Send 5 orders
		for (int i = 1; i <= 5; i++) {
			OrderEventsDTO order = OrderEventsDTO.builder()
				.orderId("LOOP-ORD-" + i)
				.customerEmail("test" + i + "@example.com")
				.customerPhone("+123456789" + i)
				.amount(50.0 * i)
				.build();
			
			restTemplate.postForEntity("/api/orders", order, OrderEventsDTO.class);
		}
		
		// Wait for async processing (consumers need time)
		Thread.sleep(3000); // 3 seconds
		
		// Assert: Check metrics increased
		MetricsDTO finalMetrics = restTemplate.getForObject(
			"/api/metrics",
			MetricsDTO.class
		);
		
		assertThat(finalMetrics.getTotalEventsProcessed())
			.isGreaterThanOrEqualTo(initialMetrics.getTotalEventsProcessed() + 5);
	}

	@Test
	void testAnalyticsServicePushesUpdates() throws InterruptedException {
		// Arrange: Get initial count
		Long initialCount = analyticsService.getMetrics().getTotalEventsProcessed();
		
		// Act: Send order
		OrderEventsDTO order = OrderEventsDTO.builder()
			.orderId("ANALYTICS-001")
			.customerEmail("test@example.com")
			.customerPhone("+1234567890")
			.amount(99.99)
			.build();
		
		restTemplate.postForEntity("/api/orders", order, OrderEventsDTO.class);
		
		// Wait for processing
		Thread.sleep(2000);
		
		// Assert: Analytics service was updated
		Long finalCount = analyticsService.getMetrics().getTotalEventsProcessed();
		assertThat(finalCount).isGreaterThan(initialCount);
		
		// This proves AnalyticsConsumer processed the message
		// and incremented the counter (which also triggers WebSocket push)
	}

}
