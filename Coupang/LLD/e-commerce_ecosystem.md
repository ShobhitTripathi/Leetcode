Yes. Once you stop looking at them as separate interview questions and start viewing them as a single e-commerce ecosystem, the whole picture becomes much easier to remember.

# Coupang E-Commerce Ecosystem Mental Model

```text
                    CUSTOMER
                         |
                         |
                    Shopping Cart
                         |
                         |
                Coupon Engine
                         |
                         |
                 Order Service
                         |
        --------------------------------
        |              |              |
        |              |              |
 Inventory       Payment       Return Service
 Reservation     Service             |
        |                            |
        |                            |
 Warehouse Mgmt               Refund Service
        |
        |
 Product Picking
        |
        |
 Packing
        |
        |
 Shipment Service
        |
        |
 Delivery Assignment
        |
        |
 Product Tracking
        |
        |
      Delivered
```

---

# Service View

This is how I'd visualize it for Staff interviews.

```text
+----------------------+
|  Coupon Service      |
+----------------------+
          |
          v

+----------------------+
|   Order Service      |
+----------------------+
      |      |      |
      |      |      |
      v      v      v

Inventory  Payment  Shipment

      |                |
      |                |
      v                v

Warehouse        Delivery Assignment

      |                |
      |                |
      +-------+--------+
              |
              v

      Product Tracking

              |
              v

      Return & Refund
```

---

# Domain Objects Map

Think of these as your primary entities.

```text
Customer
    |
    |
Order
    |
    +----- OrderItem
    |
    +----- Payment
    |
    +----- Shipment
    |
    +----- ReturnRequest

Shipment
    |
    +----- TrackingEvent

Warehouse
    |
    +----- Inventory
    |
    +----- Bin
    |
    +----- PickTask
    |
    +----- PackTask

Coupon
    |
    +----- Promotion
    |
    +----- ValidationRule
```

---

# Design Pattern Cheat Sheet

This is probably the highest ROI revision sheet.

## State Pattern

Whenever lifecycle exists.

Used In:

```text
Order Management

Return & Refund

Shipment Tracking

Delivery Task

Pick Task

Pack Task
```

Mental Trigger:

```text
Status Changes?
Lifecycle?
Allowed Transitions?
```

Use:

```java
OrderState
ReturnState
ShipmentState
```

---

## Strategy Pattern

Whenever business rules change frequently.

Used In:

```text
Coupon Engine

Delivery Assignment

Inventory Allocation

Return Policy

Bin Allocation

Rate Limiter Algorithm
```

Mental Trigger:

```text
Tomorrow business may ask
for a new variation.
```

Use:

```java
DiscountStrategy

AssignmentStrategy

RateLimitStrategy

ReturnPolicy
```

---

## Adapter Pattern

Whenever external integrations exist.

Used In:

```text
Product Tracking
```

Example:

```java
FedexAdapter

UPSAdapter

DHLAdapter
```

Mental Trigger:

```text
Different vendors
Different APIs
```

---

## Chain Of Responsibility

Multiple validations.

Used In:

```text
Coupon Validation
```

Example:

```java
ExpiryRule
      ↓
UsageRule
      ↓
CategoryRule
      ↓
MinAmountRule
```

Mental Trigger:

```text
Many validations
one after another
```

---

## Observer/Event Pattern

Decoupled notifications.

Used In:

```text
Order Created

Shipment Created

Delivered

Refund Completed
```

Example:

```java
OrderCreatedEvent

ShipmentDeliveredEvent
```

Consumers:

```text
Email Service

SMS Service

Analytics Service
```

---

# Concurrency Revision Map

Almost every Staff LLD eventually reaches one of these.

| System       | Concurrency Problem        | Solution           |
| ------------ | -------------------------- | ------------------ |
| Inventory    | Overselling                | Optimistic Locking |
| Coupon       | Last Coupon Usage          | Versioning         |
| Delivery     | Same Driver Assigned Twice | Atomic Update      |
| Tracking     | Duplicate Events           | Idempotency        |
| Refund       | Duplicate Refund           | Idempotency        |
| Rate Limiter | Multiple Requests          | Atomic Counters    |

---

# Audit Trail Revision Map

Every important business operation gets an Event table.

```text
Order
    |
    +---- OrderEvent

Inventory
    |
    +---- InventoryEvent

Shipment
    |
    +---- TrackingEvent

Return
    |
    +---- ReturnEvent

Refund
    |
    +---- RefundEvent
```

Mental Rule:

```text
Money?
Inventory?
Customer Impact?

Store Events.
```

---

# Service Ownership Map

A very common Staff-level discussion.

```text
Order Service
    Owns:
    Order Lifecycle

Inventory Service
    Owns:
    Inventory

Warehouse Service
    Owns:
    Storage Locations

Shipment Service
    Owns:
    Shipment Creation

Delivery Service
    Owns:
    Driver Assignment

Tracking Service
    Owns:
    Tracking History

Return Service
    Owns:
    Returns

Refund Service
    Owns:
    Refunds

Coupon Service
    Owns:
    Promotions

Payment Service
    Owns:
    Payments
```

---

# Interview Mental Flow

Whenever a new LLD question comes:

```text
1. Requirements

2. Core Entities

3. APIs

4. Business Rules

5. State Machine

6. Design Patterns

7. Concurrency

8. Failure Handling

9. Extensibility

10. Audit Trail

11. Service Boundaries

12. Tradeoffs
```

This is the framework you should repeatedly apply.

---

# The One Diagram I'd Memorize

```text
Coupon
   |
   v

Order
   |
   v

Inventory Reservation
   |
   v

Warehouse
   |
   v

Pick
   |
   v

Pack
   |
   v

Shipment
   |
   v

Delivery Assignment
   |
   v

Tracking
   |
   v

Delivered
   |
   v

Return
   |
   v

Refund
```

If you can walk through this flow and discuss:

* Entities
* Services
* State transitions
* Concurrency
* Patterns
* Failure handling

at each step, you'll cover the majority of the design territory expected in a Staff Engineer interview for a logistics-focused company like Coupang. This single flow effectively connects all eight LLDs into one coherent system.
