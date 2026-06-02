# 2. Order Management System (Staff-Level LLD)

---

# 1. Problem Statement

Design an Order Management System (OMS) for an e-commerce platform.

The system should support:

* Order creation
* Payment processing
* Order tracking
* Shipment tracking
* Order cancellation
* Returns and refunds
* Partial shipments
* Partial cancellations

---

# 2. Functional Requirements

### Core Requirements

* Create order
* View order details
* Update order status
* Cancel order
* Return order
* Refund order
* Track order

### Advanced Requirements

* Partial cancellation
* Partial return
* Multiple shipments per order
* Multi-warehouse fulfillment
* Order history tracking

---

# 3. Non-Functional Requirements

* High availability
* Consistent order state
* Auditability
* Extensibility
* Idempotent operations
* Scalability for millions of orders

---

# 4. Core Entities

```text
Customer
    |
    |
   Order
    |
    +------ OrderItem
    |
    +------ Payment
    |
    +------ Shipment
    |
    +------ ReturnRequest
```

---

# 5. Basic Classes (Domain Objects)

These are the entities I would first draw on the whiteboard.

## Customer

```java
public class Customer {

    private String customerId;
    private String name;
    private String email;
}
```

---

## Order

```java
public class Order {

    private String orderId;

    private String customerId;

    private List<OrderItem> items;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private Instant createdAt;
}
```

---

## OrderItem

```java
public class OrderItem {

    private String itemId;

    private String productId;

    private int quantity;

    private BigDecimal unitPrice;

    private OrderItemStatus status;
}
```

---

## Payment

```java
public class Payment {

    private String paymentId;

    private String orderId;

    private BigDecimal amount;

    private PaymentStatus status;
}
```

---

## Shipment

```java
public class Shipment {

    private String shipmentId;

    private String orderId;

    private ShipmentStatus status;

    private List<OrderItem> items;
}
```

---

## ReturnRequest

```java
public class ReturnRequest {

    private String returnId;

    private String orderId;

    private ReturnStatus status;

    private String reason;
}
```

---

# 6. Supporting Enums

## Order Status

```java
public enum OrderStatus {

    CREATED,
    PAYMENT_PENDING,
    PAID,
    PACKED,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED
}
```

---

## Order Item Status

```java
public enum OrderItemStatus {

    ACTIVE,
    CANCELLED,
    RETURNED
}
```

---

## Payment Status

```java
public enum PaymentStatus {

    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED
}
```

---

## Shipment Status

```java
public enum ShipmentStatus {

    CREATED,
    PACKED,
    SHIPPED,
    DELIVERED
}
```

---

# 7. Service APIs

## Order Service

```java
public interface OrderService {

    Order createOrder(CreateOrderRequest request);

    Order getOrder(String orderId);

    void cancelOrder(String orderId);

    void markPaid(String orderId);

    void shipOrder(String orderId);

    void deliverOrder(String orderId);
}
```

---

## Payment Service

```java
public interface PaymentService {

    Payment initiatePayment(String orderId);

    void markSuccess(String paymentId);

    void markFailure(String paymentId);

    void refund(String paymentId);
}
```

---

## Shipment Service

```java
public interface ShipmentService {

    Shipment createShipment(String orderId);

    void packShipment(String shipmentId);

    void shipShipment(String shipmentId);

    void deliverShipment(String shipmentId);
}
```

---

# 8. Business Rules

### Order Creation

```text
Order can only be created if inventory is reserved.
```

---

### Cancellation

```text
Allowed before shipment.
```

---

### Return

```text
Allowed only after delivery.
```

---

### Refund

```text
Allowed only after return approval.
```

---

### Partial Cancellation

```text
Individual order items can be cancelled.
```

---

### Partial Shipment

```text
Different items can belong to different shipments.
```

---

# 9. State Transitions

## Order Lifecycle

```text
CREATED
    ↓
PAYMENT_PENDING
    ↓
PAID
    ↓
PACKED
    ↓
SHIPPED
    ↓
DELIVERED
```

---

## Cancellation Path

```text
CREATED
    ↓
CANCELLED
```

or

```text
PAID
    ↓
CANCELLED
```

depending on business policy.

---

## Return Path

```text
DELIVERED
     ↓
RETURN_REQUESTED
     ↓
RETURN_APPROVED
     ↓
REFUNDED
```

---

# 10. Design Patterns

## State Pattern

### Problem

Avoid huge switch statements.

Bad:

```java
if(order.getStatus() == SHIPPED) {
   ...
}
```

Good:

```java
public interface OrderState {

    void cancel(Order order);

    void ship(Order order);

    void deliver(Order order);
}
```

Implementations:

```java
CreatedState
PaidState
ShippedState
DeliveredState
```

---

## Strategy Pattern

For cancellation rules.

```java
public interface CancellationPolicy {

    boolean canCancel(Order order);
}
```

Implementations:

```java
BeforePackingPolicy
BeforeShippingPolicy
PremiumCustomerPolicy
```

---

# 11. Concurrency Challenges

## Scenario 1

Two users submit same request twice.

### Solution

Idempotency key.

```java
Map<RequestId, OrderId>
```

---

## Scenario 2

Order cancellation and shipment happen simultaneously.

### Example

```text
Thread A -> cancelOrder()

Thread B -> shipOrder()
```

### Solution

Optimistic Locking.

```java
private Long version;
```

Database update:

```sql
UPDATE orders
SET status='SHIPPED',
    version=version+1
WHERE order_id=?
AND version=?
```

---

# 12. Failure Handling

## Payment Success but Order Update Fails

### Example

```text
Payment Gateway Success

Order DB Failure
```

### Solution

Saga Pattern.

```text
PaymentCompletedEvent
       ↓
OrderService
       ↓
Retry
       ↓
DLQ
```

---

## Shipment Service Down

### Solution

Event replay.

```text
OrderPaidEvent
```

stored in queue.

Process later.

---

# 13. Extensibility

## New Requirement

Support:

```text
Split Shipment
```

Current design already supports:

```java
List<Shipment>
```

---

## New Requirement

Support:

```text
International Orders
```

Add:

```java
ShippingPolicy
```

without modifying Order.

---

## New Requirement

Support:

```text
Scheduled Delivery
```

Add:

```java
DeliveryPreference
```

inside Shipment.

---

# 14. Auditability

Never overwrite state without history.

## OrderEvent

```java
public class OrderEvent {

    private String eventId;

    private String orderId;

    private OrderStatus oldStatus;

    private OrderStatus newStatus;

    private Instant timestamp;
}
```

---

Examples:

```text
CREATED -> PAID

PAID -> SHIPPED

SHIPPED -> DELIVERED
```

---

# 15. Service Boundaries

This is very important at Staff level.

## Order Service

Owns:

```text
Order Lifecycle
```

---

## Inventory Service

Owns:

```text
Inventory Reservation
Inventory Release
```

---

## Payment Service

Owns:

```text
Payment Processing
Refunds
```

---

## Shipment Service

Owns:

```text
Packing
Shipping
Delivery
```

---

# 16. Database Tables

```sql
ORDERS
```

```sql
ORDER_ITEMS
```

```sql
PAYMENTS
```

```sql
SHIPMENTS
```

```sql
RETURNS
```

```sql
ORDER_EVENTS
```

---

# 17. Trade-Offs

## Status at Order Level vs Derived

### Option 1

Store status directly.

```java
order.status
```

Pros:

* Fast reads

Cons:

* Data duplication

---

### Option 2

Calculate from items.

Pros:

* Single source of truth

Cons:

* Expensive queries

---

### Choice

For large e-commerce systems, store status explicitly and maintain it through events.

---

# 18. Common Staff-Level Follow-Up Questions

### Q1. How do you support partial cancellation?

**Answer:** Maintain status at `OrderItem` level and derive aggregate order status.

---

### Q2. How do you support multiple warehouses?

**Answer:** Introduce `FulfillmentPlan` and allocate items across warehouses.

---

### Q3. How do you prevent duplicate order creation?

**Answer:** Idempotency keys.

---

### Q4. How do you recover if payment succeeds but OMS crashes?

**Answer:** Saga pattern + retry + event-driven processing.

---

### Q5. How do you track every state change?

**Answer:** `OrderEvent` audit table.

---

### Q6. How do you notify customers?

**Answer:** Publish domain events (`OrderShippedEvent`, `OrderDeliveredEvent`) and let Email/SMS/Push services consume them.

---

This is the level of structure I would use in a Coupang Staff interview. It shows not only class design, but also domain understanding, operational concerns, and distributed-system thinking. The next system I'd tackle is **Warehouse Management System**, because that is one of the most supply-chain-centric designs and often leads to deeper discussions in logistics-focused companies.
