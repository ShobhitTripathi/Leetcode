Good approach. For a Staff Engineer interview, don't just practice drawing classes. Practice the **discussion flow**:

1. Clarify requirements.
2. Identify entities.
3. Design APIs.
4. Design class model.
5. Discuss patterns.
6. Discuss concurrency/failures.
7. Discuss scalability and future extensions.

Let's start with the most important one.

# 1. Inventory Reservation System

This is arguably the highest-value LLD topic for Coupang because inventory consistency is at the heart of e-commerce and supply-chain systems.

---

# Problem Statement

Design an Inventory Reservation System.

When a customer places an order:

1. Inventory should be reserved.
2. Reserved inventory should not be sold to others.
3. If payment succeeds, inventory is committed.
4. If payment fails, inventory is released.
5. Reservation expires after some time (e.g., 15 minutes).

Example:

```text
Inventory = 10 iPhones

User A adds 3 to cart
Reserve 3

Available = 7
Reserved = 3

Payment success
Commit reservation

Available = 7
Reserved = 0
Sold = 3
```

---

# Clarifying Questions

Before coding, ask:

### Is inventory tracked per warehouse?

Usually yes.

```text
Product A
Warehouse X -> 100

Warehouse Y -> 50
```

---

### Can one reservation span multiple warehouses?

Possible follow-up.

---

### Does reservation expire?

Yes.

Typically:

```text
15 min
30 min
```

---

### Can inventory be updated manually?

Yes.

For audits:

```text
Damaged item
Lost item
Found item
```

---

# Core Entities

## Product

```java
class Product {
    String productId;
    String sku;
    String name;
}
```

---

## Warehouse

```java
class Warehouse {
    String warehouseId;
    String name;
}
```

---

## Inventory

Represents stock for a product in a warehouse.

```java
class Inventory {
    String productId;
    String warehouseId;

    int availableQty;
    int reservedQty;

    long version;
}
```

Notice:

```text
availableQty
reservedQty
```

instead of

```text
totalQty
```

because reservation becomes simpler.

---

## Reservation

```java
class Reservation {
    String reservationId;

    String productId;

    int quantity;

    ReservationStatus status;

    Instant createdAt;
    Instant expiresAt;
}
```

---

## Reservation Status

```java
enum ReservationStatus {
    ACTIVE,
    COMMITTED,
    EXPIRED,
    CANCELLED
}
```

---

# Service APIs

Interviewers expect this.

```java
interface InventoryService {

    Reservation reserve(
        String productId,
        int qty
    );

    void commit(
        String reservationId
    );

    void release(
        String reservationId
    );

    Inventory getInventory(
        String productId
    );
}
```

---

# Reservation Flow

## Step 1

Reserve inventory.

Current:

```text
available = 10
reserved = 0
```

User reserves 3.

After:

```text
available = 7
reserved = 3
```

---

# Commit Flow

Payment success.

Current:

```text
available = 7
reserved = 3
```

Commit:

```text
available = 7
reserved = 0
```

Inventory already reduced.

Only reservation gets finalized.

---

# Release Flow

Payment failed.

Current:

```text
available = 7
reserved = 3
```

After release:

```text
available = 10
reserved = 0
```

---

# Biggest Interview Follow-Up

## Concurrent Reservations

Inventory:

```text
available = 1
```

Two requests:

```text
reserve(1)
reserve(1)
```

arrive simultaneously.

Without protection:

```text
both succeed
```

Overselling occurs.

---

# Solution 1: Optimistic Locking

Inventory table:

```java
class Inventory {
    long version;
}
```

Update query:

```sql
UPDATE inventory
SET available_qty = 0,
    reserved_qty = 1,
    version = version + 1
WHERE id = ?
AND version = ?
```

Only one succeeds.

The other retries.

This is usually the answer interviewers want.

---

# Solution 2: Distributed Lock

For distributed systems:

```text
Redis Lock
ZooKeeper
```

Mention it.

Then say:

> For inventory systems I generally prefer optimistic locking because contention is usually manageable and lock management overhead is avoided.

This sounds senior.

---

# Reservation Expiry

Reservation:

```text
expiresAt
```

Background worker:

```text
every minute
```

Find:

```sql
ACTIVE reservations
where expires_at < NOW()
```

Release inventory.

Mark:

```text
EXPIRED
```

---

# Audit Trail (Staff-Level Discussion)

Never directly modify inventory without tracking.

Create:

```java
class InventoryEvent {

    String eventId;

    String productId;

    EventType type;

    int quantity;

    Instant timestamp;
}
```

---

Types:

```java
RESERVE
RELEASE
COMMIT
ADJUSTMENT
```

Now every inventory change becomes traceable.

Interviewers love this.

---

# Design Patterns

You don't need many.

Useful:

### Strategy Pattern

```java
interface ReservationPolicy
```

Implementations:

```java
SimpleReservationPolicy

WarehouseAwareReservationPolicy

PriorityCustomerReservationPolicy
```

Allows future extension.

---

# Database Tables

A strong answer should mention these:

```sql
PRODUCT
```

```sql
WAREHOUSE
```

```sql
INVENTORY
```

```sql
RESERVATION
```

```sql
INVENTORY_EVENT
```

---

# Staff-Level Follow-Up

If interviewer asks:

> We have inventory in 20 warehouses. Customer orders 10 units. No single warehouse has 10 units. What will you do?

Discuss:

```text
Inventory Allocation Strategy
```

Example:

```text
Warehouse A -> 4

Warehouse B -> 3

Warehouse C -> 3
```

Create:

```java
ReservationLine
```

inside Reservation.

This is where the discussion starts moving from LLD toward HLD.

---

### What I'd expect in a Coupang Staff interview

After you finish the basic design, the interviewer will likely spend 70% of the time on:

* Overselling prevention
* Reservation expiry
* Multi-warehouse allocation
* Audit trail
* High concurrency
* Inventory consistency vs performance

Those are the areas worth mastering.

Next, we can do **Order Management System**, which naturally connects to Inventory Reservation and is probably the second most likely Coupang LLD question.
