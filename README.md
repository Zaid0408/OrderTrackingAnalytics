# Order Tracking & Analytics System

A real-time event-driven notification system built with Spring Boot, RabbitMQ, and WebSocket.

## What Does This Project Solve?

**Problem:** When an order is placed, multiple independent actions need to happen:
- Send email confirmation
- Send SMS notification
- Update real-time analytics dashboard

**Solution:** Uses a **message broker (RabbitMQ)** to decouple these operations. If the email service fails, SMS and analytics still work.

---
## Architecture Overview

### **Message Flow:**
```
User → REST API → RabbitMQ Fanout Exchange → 3 Independent Queues
                                            ├─> Email Queue → Email Consumer
                                            ├─> SMS Queue → SMS Consumer
                                            └─> Analytics Queue → Analytics Consumer
```

### **Key Pattern: Fanout Exchange**
- **One event** published by Producer
- **Three copies** sent to different queues
- **Three consumers** process independently

---

## Project Structure & Responsibilities

### **1. Configuration Layer**
| Class | Purpose |
|-------|---------|
| `RabbitMQConfig` | Defines fanout exchange, 3 queues, and bindings |
| `WebSocketConfig` | Enables WebSocket for real-time dashboard updates |

### **2. Controller Layer (REST API)**
| Class | Endpoint | Purpose |
|-------|----------|---------|
| `OrderController` | `POST /api/orders` | Receives order and publishes to RabbitMQ |
| `MetricsController` | `GET /api/metrics` | Returns current analytics counts |

### **3. Producer Layer**
| Class | Purpose |
|-------|---------|
| `OrderEventProducer` | Publishes `OrderEventsDTO` to fanout exchange |

### **4. Consumer Layer (Message Processors)**
| Class | Queue | Purpose |
|-------|-------|---------|
| `EmailNotificationConsumer` | `queue.fanout.email` | Simulates email sending, saves log, increments counter |
| `SmsNotificationConsumer` | `queue.fanout.sms` | Simulates SMS sending, saves log, increments counter |
| `AnalyticsConsumer` | `queue.fanout.analytics` | Increments event counter, pushes WebSocket update |

### **5. Service Layer (Business Logic)**
| Class | Purpose |
|-------|---------|
| `NotificationService` | Saves notification logs to H2 database |
| `AnalyticsService` | Maintains in-memory counters (email, SMS, events) |
| `WebSocketService` | Pushes real-time metrics to `/topic/metrics` |

### **6. Data Layer**
| Class | Type | Purpose |
|-------|------|---------|
| `OrderEventsDTO` | DTO | Payload for order events (contains email, phone, amount) |
| `MetricsDTO` | DTO | Analytics response (counters + timestamp) |
| `NotificationLog` | Entity | Database table storing notification history |
| `NotificationLogRepository` | Repository | JPA interface for database operations |

---

## Complete Flow (Step-by-Step)

### **Flow 1: Order Placement → Notifications**
```
1. User sends POST /api/orders with order details
   ↓
2. OrderController receives request
   ↓
3. OrderController calls OrderEventProducer
   ↓
4. Producer publishes OrderEventsDTO to fanout exchange
   ↓
5. RabbitMQ copies message to 3 queues:
   
   Queue 1 (email.queue):
   ├─> EmailNotificationConsumer receives message
   ├─> Simulates email sending
   ├─> Calls NotificationService to save log (type: EMAIL, status: SENT)
   └─> Calls AnalyticsService.incrementEmailCount()
   
   Queue 2 (sms.queue):
   ├─> SmsNotificationConsumer receives message
   ├─> Simulates SMS sending
   ├─> Calls NotificationService to save log (type: SMS, status: SENT)
   └─> Calls AnalyticsService.incrementSmsCount()
   
   Queue 3 (analytics.queue):
   ├─> AnalyticsConsumer receives message
   ├─> Calls AnalyticsService.incrementEventCount()
   ├─> Gets updated MetricsDTO from AnalyticsService
   └─> Calls WebSocketService to push update to /topic/metrics
```

### **Flow 2: Dashboard Metrics**
```
Option A (REST API - Pull):
GET /api/metrics → MetricsController → AnalyticsService → Returns current counts

Option B (WebSocket - Push):
Frontend subscribes to /topic/metrics → Receives automatic updates when counters change
```

---

## Technologies Used

- **Spring Boot 3.2.x** - Application framework
- **RabbitMQ (CloudAMQP)** - Message broker
- **H2 Database** - In-memory database for logs
- **WebSocket (STOMP)** - Real-time communication
- **Lombok** - Reduces boilerplate code
- **Spring Data JPA** - Database operations

---

## Key Learning Outcomes

1. **Fanout Exchange Pattern** - One message, multiple consumers
2. **System Decoupling** - Services fail independently
3. **Real-time Updates** - WebSocket push notifications
4. **Message-Driven Architecture** - Async processing with RabbitMQ
5. **Event-Driven Design** - Producers and consumers communicate via events

---

## Database Schema

### `notification_logs` Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto-generated ID |
| order_id | VARCHAR | Order identifier from event |
| notification_type | VARCHAR | EMAIL or SMS |
| status | VARCHAR | SENT or FAILED |
| sent_at | TIMESTAMP | When notification was processed |

---

## Why This Architecture?

**Scenario:** Email service is down

**Without Message Broker:**
- Order placement fails ❌
- User sees error ❌
- No SMS sent ❌
- No analytics ❌

**With Message Broker (This Project):**
- Order accepted ✅
- Message queued ✅
- Email fails, but retries automatically ✅
- SMS still sent ✅
- Analytics still updated ✅
- User doesn't see error ✅

---

## Future Enhancements

- Add retry logic for failed notifications
- Implement dead-letter queue for failed messages
- Add authentication to WebSocket endpoints
- Switch to PostgreSQL for production
- Add monitoring dashboard UI
