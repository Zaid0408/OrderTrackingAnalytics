# Order Tracking & Analytics System

A real-time event-driven notification system built with Spring Boot, RabbitMQ, and WebSocket demonstrating message broker patterns and asynchronous processing.

## Problem Statement

When an order is placed, multiple independent services need to be notified (email, SMS, analytics). Traditional synchronous calls create tight coupling - if one service fails, the entire operation fails.

## Solution

Uses RabbitMQ **Fanout Exchange** to decouple services. One event triggers multiple independent consumers. If email fails, SMS and analytics still work.

---

## Architecture

**Message Flow:**

REST API → Producer → Fanout Exchange → 3 Queues
├─> Email Consumer
├─> SMS Consumer
└─> Analytics Consumer → WebSocket Push

**Key Pattern:** Fanout Exchange (1 message → 3 copies → 3 independent consumers)

---

## Tech Stack

- **Spring Boot 3.2.x** - Application framework
- **RabbitMQ (CloudAMQP)** - Message broker
- **H2 Database** - Notification logs storage
- **WebSocket (STOMP)** - Real-time analytics updates
- **Lombok** - Reduces boilerplate

---

## Project Structure

### **Configuration**
- `RabbitMQConfig` - Creates fanout exchange, 3 queues, bindings, JSON converter
- `WebSocketConfig` - Enables WebSocket messaging at `/topic/metrics`

### **Controllers** 
- `OrderController` - `POST /api/orders` - Receives orders, publishes to RabbitMQ
- `MetricsController` - `GET /api/metrics` - Returns current analytics counts

### **Producer**
- `OrderEventProducer` - Publishes `OrderEventsDTO` to fanout exchange

### **Consumers**
- `EmailNotificationConsumer` - Processes email queue, saves logs, increments counter
- `SmsNotificationConsumer` - Processes SMS queue, saves logs, increments counter
- `AnalyticsConsumer` - Increments event counter, pushes WebSocket update

### **Services**
- `NotificationService` - Saves notification logs to H2 database
- `AnalyticsService` - Maintains in-memory counters (email, SMS, events)
- `WebSocketService` - Broadcasts metrics to `/topic/metrics` channel

### **Data Models**
- `OrderEventsDTO` - Order event payload (orderId, email, phone, amount)
- `MetricsDTO` - Analytics response (counters, timestamp)
- `NotificationLog` (Entity) - Database table for notification history

### **Repository**
- `NotificationLogRepository` - JPA interface for database operations

---

## How It Works

1. **POST** order to `/api/orders`
2. `OrderController` calls `OrderEventProducer`
3. Producer sends message to **fanout exchange**
4. RabbitMQ **copies message to 3 queues**
5. **3 consumers process independently:**
   - Email consumer → simulates email → saves log → increments counter
   - SMS consumer → simulates SMS → saves log → increments counter
   - Analytics consumer → increments event counter → pushes WebSocket update
6. **GET** `/api/metrics` to see current counts
7. WebSocket clients receive **real-time updates** automatically

---

## Setup & Run

### **1. Configure RabbitMQ**
Create CloudAMQP instance, update `application.properties`:
```properties
spring.rabbitmq.addresses=amqps://your-instance.cloudamqp.com/vhost
```

### **2. Run Application**
```bash
./mvnw spring-boot:run
```

### **3. Test**
```bash
# Send order
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"orderId":"ORD-001","customerEmail":"test@example.com","customerPhone":"+1234567890","amount":99.99}'

# Check metrics
curl http://localhost:8080/api/metrics
```

### **4. Verify Database**
- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:orderdb`
- Query: `SELECT * FROM notification_log;`

---

## Key Learning Outcomes

- **Fanout Pattern** - One event, multiple independent consumers
- **System Decoupling** - Services fail independently without affecting others
- **Message-Driven Architecture** - Asynchronous processing with RabbitMQ
- **Real-Time Updates** - WebSocket push notifications
- **Event-Driven Design** - Producers and consumers communicate via events

---

## Database Schema

**notification_log**

id                 BIGINT (PK)
order_id           VARCHAR
notification_type  ENUM (EMAIL, SMS)
status             VARCHAR (SENT, FAILED)
sent_at            TIMESTAMP

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create order and trigger notifications |
| GET | `/api/metrics` | Get current analytics (email, SMS, event counts) |
| WebSocket | `/topic/metrics` | Subscribe for real-time metric updates |
| GET | `/h2-console` | Access H2 database console |

---

## Why This Architecture?

**Without Message Broker:**
- Email service down → entire order fails ❌
- Tight coupling between services ❌
- No retry mechanism ❌

**With Message Broker (This Project):**
- Email fails → SMS and analytics still work ✅
- Services are decoupled ✅
- Automatic retries ✅
- Scalable (add more consumers easily) ✅

---

## Author

Built as a learning project to demonstrate event-driven architecture and message broker patterns.