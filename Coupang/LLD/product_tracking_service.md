# 5. Product Tracking Service (Staff-Level LLD)

This is a very common e-commerce/logistics problem.

The goal is to allow customers, support teams, and internal systems to track a shipment throughout its lifecycle.

---

# 1. Problem Statement

Design a Product Tracking Service that:

* Tracks shipment status
* Stores tracking history
* Supports real-time status updates
* Supports multiple carriers
* Exposes shipment tracking APIs

Example:

```text
Order Placed
    ↓
Packed
    ↓
Shipped
    ↓
In Transit
    ↓
Out For Delivery
    ↓
Delivered
```

---

# 2. Functional Requirements

### Customer Features

* Track shipment
* View current status
* View tracking history

### Internal Features

* Update tracking status
* Register carrier updates
* Handle delivery failures

### Carrier Integration

* Support multiple carriers
* Receive tracking events

---

# 3. Non-Functional Requirements

* High write throughput
* Fast tracking lookups
* Complete audit history
* Eventual consistency acceptable
* Support millions of tracking events/day

---

# 4. Core Entities

```text
Order
   |
Shipment
   |
TrackingEvent
   |
Carrier
```

---

# 5. Basic Classes

## Carrier

```java
public class Carrier {

    private String carrierId;

    private String name;
}
```

---

## Shipment

```java
public class Shipment {

    private String shipmentId;

    private String orderId;

    private String carrierId;

    private TrackingStatus currentStatus;

    private Instant createdAt;
}
```

---

## TrackingEvent

This is the most important entity.

```java
public class TrackingEvent {

    private String eventId;

    private String shipmentId;

    private TrackingStatus status;

    private String location;

    private Instant eventTime;
}
```

---

## TrackingResponse

```java
public class TrackingResponse {

    private Shipment shipment;

    private List<TrackingEvent> history;
}
```

---

# 6. Supporting Enums

## Tracking Status

```java
public enum TrackingStatus {

    CREATED,

    PACKED,

    SHIPPED,

    IN_TRANSIT,

    OUT_FOR_DELIVERY,

    DELIVERED,

    FAILED,

    RETURNED
}
```

---

# 7. Service APIs

## Tracking Service

```java
public interface TrackingService {

    TrackingResponse trackShipment(
            String shipmentId);

    void updateTrackingStatus(
            TrackingUpdateRequest request);

    List<TrackingEvent> getHistory(
            String shipmentId);
}
```

---

## Carrier Service

```java
public interface CarrierService {

    void registerCarrier(
            Carrier carrier);

    void processCarrierUpdate(
            CarrierUpdate update);
}
```

---

# 8. Business Rules

### Shipment Creation

```text
Tracking begins only after shipment creation.
```

---

### Delivery

```text
Delivered shipment cannot move back to transit.
```

---

### Failed Delivery

```text
Failed deliveries can be retried.
```

---

### History

```text
Tracking events are immutable.
```

Never update old events.

Append new events.

---

# 9. State Transitions

## Normal Flow

```text
CREATED
    ↓
PACKED
    ↓
SHIPPED
    ↓
IN_TRANSIT
    ↓
OUT_FOR_DELIVERY
    ↓
DELIVERED
```

---

## Failure Flow

```text
OUT_FOR_DELIVERY
        ↓
FAILED
```

---

## Return Flow

```text
DELIVERED
      ↓
RETURNED
```

---

# 10. Design Patterns

## State Pattern

Useful when transitions become complex.

```java
public interface ShipmentState {

    void transit();

    void deliver();

    void fail();
}
```

---

## Adapter Pattern

Very important.

Every carrier exposes different APIs.

Example:

```text
FedEx API
UPS API
DHL API
```

Create:

```java
public interface CarrierAdapter {

    TrackingEvent fetchStatus(
            String trackingNumber);
}
```

Implementations:

```java
FedExAdapter

UPSAdapter

DHLAdapter
```

---

# 11. Concurrency Challenges

## Scenario 1

Two tracking updates arrive simultaneously.

Example:

```text
IN_TRANSIT

OUT_FOR_DELIVERY
```

at almost same time.

---

### Solution

Store event timestamp.

Apply only newer update.

```java
private Instant eventTime;
```

---

## Scenario 2

Carrier sends duplicate updates.

Example:

```text
IN_TRANSIT

IN_TRANSIT

IN_TRANSIT
```

---

### Solution

Idempotency.

```java
eventId
```

or

```text
shipmentId + timestamp + status
```

deduplication key.

---

# 12. Failure Handling

## Carrier API Down

Problem:

```text
Unable to fetch updates.
```

---

### Solution

Retry.

```text
Exponential Backoff
```

and store failed requests.

---

## Tracking Update Lost

Solution:

```text
Append-only event log
```

Replay later.

---

## Database Failure

Solution:

Persist events to queue first.

```text
Kafka
```

then consume.

(For HLD discussion.)

---

# 13. Extensibility

## New Carrier Added

Current design:

```java
CarrierAdapter
```

Just add:

```java
BlueDartAdapter
```

No changes elsewhere.

---

## Real-Time Notifications

Add:

```java
ShipmentStatusChangedEvent
```

Consumers:

```text
Email Service

SMS Service

Push Service
```

---

## ETA Prediction

Add:

```java
EstimatedDeliveryService
```

without modifying tracking logic.

---

# 14. Auditability

Tracking systems require complete history.

## TrackingEvent

```java
public class TrackingEvent {

    private String eventId;

    private String shipmentId;

    private TrackingStatus status;

    private String location;

    private Instant eventTime;
}
```

---

Example:

```text
10:00 AM SHIPPED

03:00 PM IN_TRANSIT

08:00 PM OUT_FOR_DELIVERY

09:15 PM DELIVERED
```

Never overwrite.

Always append.

---

# 15. Service Boundaries

## Shipment Service

Owns:

```text
Shipment creation
```

---

## Tracking Service

Owns:

```text
Tracking history
Current status
```

---

## Carrier Integration Service

Owns:

```text
Carrier APIs
```

---

## Notification Service

Owns:

```text
Customer notifications
```

---

# 16. Database Tables

```sql
SHIPMENT
```

Current shipment state.

---

```sql
TRACKING_EVENT
```

Full history.

---

```sql
CARRIER
```

Carrier metadata.

---

# 17. Trade-Offs

## Store Current Status Only

Pros:

* Simple

Cons:

* No history

---

## Store Complete Event History

Pros:

* Auditability
* Analytics
* Replay capability

Cons:

* More storage

---

### Choice

Store:

```text
SHIPMENT.current_status
```

for fast reads

and

```text
TRACKING_EVENT
```

for complete history.

This is how most large logistics systems work.

---

# 18. Common Staff-Level Follow-Up Questions

### Q1. How do you support millions of tracking updates daily?

**Answer:**

Use append-only events and asynchronous processing.

---

### Q2. Why keep both Shipment Status and Tracking Events?

**Answer:**

Current status provides fast reads; events provide history and auditability.

---

### Q3. How do you handle duplicate carrier updates?

**Answer:**

Idempotency and deduplication keys.

---

### Q4. What if carrier reports statuses out of order?

Example:

```text
DELIVERED

then

IN_TRANSIT
```

**Answer:**

Validate transition rules and ignore stale updates.

---

### Q5. How do customers get notified?

**Answer:**

Publish `TrackingStatusChangedEvent`.

Consumers:

```text
Email
SMS
Push
```

---

### Q6. How do you support multiple carriers?

**Answer:**

Adapter Pattern around carrier APIs.

---

# Staff-Level Discussion You Should Bring Up

Most candidates design:

```text
Shipment
TrackingEvent
```

and stop.

A stronger Staff answer discusses:

### Event History

```text
Append-only
Immutable
Replayable
```

### Carrier Integrations

```text
Adapter Pattern
```

### Idempotency

```text
Duplicate Updates
```

### Out-of-Order Events

```text
Timestamp Validation
```

### Notification Hooks

```text
TrackingStatusChangedEvent
```

### Fast Read vs Full History

```text
Shipment Table
+
TrackingEvent Table
```

These are exactly the kinds of operational concerns that logistics-heavy companies expect experienced engineers to think about. Next would be **Return & Refund System**, which is another workflow-heavy design with rich state transitions and business rules.
