# 3. Warehouse Management System (WMS) – Staff-Level LLD

This is one of the most relevant designs for a company like Coupang because it sits directly in the fulfillment pipeline.

A good answer demonstrates understanding of:

* Inventory storage
* Bin allocation
* Picking
* Packing
* Inventory movement
* Fulfillment efficiency

---

# 1. Problem Statement

Design a Warehouse Management System that supports:

* Receiving inventory
* Storing inventory
* Inventory lookup
* Picking products
* Packing orders
* Inventory transfer
* Inventory adjustments

---

# 2. Functional Requirements

### Inbound

* Receive inventory from suppliers
* Allocate inventory to warehouse locations

### Inventory Management

* Track inventory quantity
* Move inventory between locations
* Adjust damaged/lost inventory

### Fulfillment

* Generate picking tasks
* Generate packing tasks
* Reserve inventory

### Outbound

* Ship products
* Update inventory

---

# 3. Non-Functional Requirements

* Inventory accuracy
* Auditability
* High throughput
* Extensibility
* Concurrent warehouse operations
* Real-time inventory visibility

---

# 4. Core Entities

```text
Warehouse
    |
    +---- Zone
            |
            +---- Shelf
                    |
                    +---- Bin
                            |
                            +---- Inventory
```

Fulfillment side:

```text
Order
    |
    +---- PickTask
    |
    +---- PackTask
    |
    +---- Shipment
```

---

# 5. Basic Classes

## Warehouse

```java
public class Warehouse {

    private String warehouseId;

    private String name;

    private List<Zone> zones;
}
```

---

## Zone

Example:

```text
Electronics
Groceries
Fashion
```

```java
public class Zone {

    private String zoneId;

    private String name;
}
```

---

## Shelf

```java
public class Shelf {

    private String shelfId;

    private String zoneId;
}
```

---

## Bin

Actual storage location.

```java
public class Bin {

    private String binId;

    private String shelfId;

    private int capacity;

    private int availableCapacity;
}
```

---

## Inventory

```java
public class Inventory {

    private String productId;

    private String warehouseId;

    private String binId;

    private int quantity;

    private Long version;
}
```

---

## PickTask

```java
public class PickTask {

    private String taskId;

    private String orderId;

    private String pickerId;

    private PickStatus status;

    private List<PickLine> items;
}
```

---

## PickLine

```java
public class PickLine {

    private String productId;

    private String binId;

    private int quantity;
}
```

---

## PackTask

```java
public class PackTask {

    private String taskId;

    private String orderId;

    private PackStatus status;
}
```

---

# 6. Supporting Enums

```java
public enum PickStatus {

    CREATED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
```

---

```java
public enum PackStatus {

    CREATED,
    PACKED,
    SHIPPED
}
```

---

# 7. Service APIs

## Inventory Service

```java
public interface InventoryService {

    void receiveInventory(
            String productId,
            int quantity);

    void moveInventory(
            String sourceBin,
            String targetBin,
            int quantity);

    void adjustInventory(
            String productId,
            int quantity);

    Inventory getInventory(
            String productId);
}
```

---

## Picking Service

```java
public interface PickingService {

    PickTask createPickTask(
            String orderId);

    void startTask(
            String taskId);

    void completeTask(
            String taskId);
}
```

---

## Packing Service

```java
public interface PackingService {

    PackTask createPackTask(
            String orderId);

    void completePackTask(
            String taskId);
}
```

---

# 8. Business Rules

### Receiving Inventory

```text
Inventory must be assigned to a valid bin.
```

---

### Picking

```text
Cannot pick more than available quantity.
```

---

### Packing

```text
Pack only after pick completion.
```

---

### Shipment

```text
Ship only after packing.
```

---

### Inventory Movement

```text
Destination bin must have capacity.
```

---

# 9. State Transitions

## Pick Task

```text
CREATED
    ↓
IN_PROGRESS
    ↓
COMPLETED
```

---

## Pack Task

```text
CREATED
    ↓
PACKED
    ↓
SHIPPED
```

---

# 10. Design Patterns

## Strategy Pattern

### Bin Allocation Strategy

```java
public interface BinAllocationStrategy {

    Bin allocate(
            Product product,
            int quantity);
}
```

Implementations:

```java
NearestAvailableBinStrategy

SameCategoryBinStrategy

LeastFilledBinStrategy
```

---

## Picking Strategy

```java
public interface PickingStrategy {

    List<PickLine> generatePickPath(
            Order order);
}
```

Implementations:

```java
ShortestDistanceStrategy

LeastCongestedStrategy
```

---

# 11. Concurrency Challenges

## Scenario 1

Two workers attempt to pick the last item.

Current:

```text
Quantity = 1
```

Workers:

```text
Worker A -> Pick

Worker B -> Pick
```

---

### Solution

Optimistic Locking.

```java
private Long version;
```

Update:

```sql
UPDATE inventory
SET quantity = quantity - 1,
    version = version + 1
WHERE version = ?
```

Only one succeeds.

---

## Scenario 2

Inventory transfer and inventory adjustment happen simultaneously.

Example:

```text
Transfer 50 items

Adjust -10 items
```

Solution:

```text
Versioning
Event Ledger
```

---

# 12. Failure Handling

## Pick Completed but Inventory Update Failed

Problem:

```text
Physical stock removed

DB update failed
```

---

### Solution

Inventory Movement Events

```java
InventoryEvent
```

Replay later.

---

## Packing Completed but Shipment Service Down

Solution:

```text
PackCompletedEvent
```

Retry until shipment created.

---

# 13. Extensibility

## New Requirement

Robot Pickers.

Current:

```java
pickerId
```

becomes:

```java
resourceId
```

Supports:

```text
Human
Robot
```

---

## New Requirement

Cold Storage Zone.

Add:

```java
StorageCondition
```

```java
FROZEN
CHILLED
AMBIENT
```

No major redesign.

---

## New Requirement

Hazardous Material Handling.

Add:

```java
HandlingPolicy
```

---

# 14. Auditability

Never directly modify inventory.

Create:

## InventoryEvent

```java
public class InventoryEvent {

    private String eventId;

    private String productId;

    private String warehouseId;

    private InventoryEventType type;

    private int quantity;

    private Instant timestamp;
}
```

---

Types:

```java
RECEIVE
PICK
PACK
MOVE
ADJUST
SHIP
```

---

# 15. Service Boundaries

## Warehouse Service

Owns:

```text
Warehouse
Zone
Shelf
Bin
```

---

## Inventory Service

Owns:

```text
Stock
Reservations
Adjustments
```

---

## Picking Service

Owns:

```text
Pick Tasks
```

---

## Packing Service

Owns:

```text
Pack Tasks
```

---

## Shipment Service

Owns:

```text
Shipment Creation
Delivery Handoff
```

---

# 16. Database Tables

```sql
WAREHOUSE
```

```sql
ZONE
```

```sql
SHELF
```

```sql
BIN
```

```sql
INVENTORY
```

```sql
PICK_TASK
```

```sql
PICK_LINE
```

```sql
PACK_TASK
```

```sql
INVENTORY_EVENT
```

---

# 17. Trade-Offs

## Store Inventory Per Warehouse

```text
Product A -> 500 units
```

Pros:

* Simpler

Cons:

* No location visibility

---

## Store Inventory Per Bin

```text
Bin A -> 20

Bin B -> 50

Bin C -> 30
```

Pros:

* Precise picking

Cons:

* More records

---

### Choice

For a large fulfillment company, inventory should be tracked at the **bin level**.

---

# 18. Common Staff-Level Follow-Up Questions

### Q1. How would you optimize picker efficiency?

**Answer:**
Generate pick paths using shortest-route algorithms and batch multiple orders into a single pick run.

---

### Q2. How would you support inventory spread across multiple bins?

**Answer:**
Create multiple `PickLine`s for the same product from different bins.

---

### Q3. How would you handle damaged inventory?

**Answer:**
Inventory adjustment event + audit trail.

---

### Q4. How do you prevent two pickers from picking the same item?

**Answer:**
Inventory reservation + optimistic locking.

---

### Q5. How would you support robotic warehouses?

**Answer:**
Abstract picker as a `Resource` and use pluggable picking strategies.

---

### Q6. What is the biggest performance bottleneck in warehouses?

**Answer:**
Picking. In many warehouses, picking consumes the majority of fulfillment time. Optimizing pick path generation and batching yields the largest operational gains.

---

### Staff-Level Insight

Many candidates design warehouses as just:

```text
Warehouse
Inventory
Product
```

A stronger answer models the **actual fulfillment workflow**:

```text
Receive Inventory
      ↓
Store in Bin
      ↓
Reserve Inventory
      ↓
Generate Pick Task
      ↓
Pick
      ↓
Pack
      ↓
Ship
```

That workflow-centric view is much closer to how logistics companies like Coupang think about warehouse systems.
