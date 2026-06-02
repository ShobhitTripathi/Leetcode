# 4. Delivery Assignment System (Staff-Level LLD)

This is another highly relevant domain for Coupang because the last-mile delivery experience is a major differentiator.

The focus is not just assigning a delivery agent. The real challenges are:

* Efficient assignment
* Reassignment
* Capacity management
* Real-time location updates
* Delivery lifecycle
* Route optimization

---

# 1. Problem Statement

Design a Delivery Assignment System that:

* Assigns delivery tasks to drivers
* Tracks delivery progress
* Supports reassignment
* Supports multiple assignment strategies
* Tracks driver capacity

---

# 2. Functional Requirements

### Driver Management

* Register driver
* Update availability
* Update location

### Delivery Assignment

* Create delivery task
* Assign driver
* Reassign driver
* Complete delivery

### Tracking

* Track task status
* Track driver location

---

# 3. Non-Functional Requirements

* Real-time assignment
* Low latency
* High availability
* Extensible assignment algorithms
* Scalable for thousands of deliveries per minute

---

# 4. Core Entities

```text
Driver
    |
    |
DeliveryTask
    |
    |
Route
```

---

# 5. Basic Classes

## Driver

```java
public class Driver {

    private String driverId;

    private String name;

    private DriverStatus status;

    private GeoLocation currentLocation;

    private int maxCapacity;

    private int assignedTasks;
}
```

---

## GeoLocation

```java
public class GeoLocation {

    private double latitude;

    private double longitude;
}
```

---

## DeliveryTask

```java
public class DeliveryTask {

    private String taskId;

    private String shipmentId;

    private String driverId;

    private GeoLocation pickupLocation;

    private GeoLocation deliveryLocation;

    private DeliveryStatus status;
}
```

---

## Route

```java
public class Route {

    private String routeId;

    private String driverId;

    private List<DeliveryTask> tasks;
}
```

---

# 6. Supporting Enums

## Driver Status

```java
public enum DriverStatus {

    AVAILABLE,
    BUSY,
    OFFLINE
}
```

---

## Delivery Status

```java
public enum DeliveryStatus {

    CREATED,
    ASSIGNED,
    PICKED_UP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED
}
```

---

# 7. Service APIs

## Driver Service

```java
public interface DriverService {

    void registerDriver(
            Driver driver);

    void updateLocation(
            String driverId,
            GeoLocation location);

    void updateStatus(
            String driverId,
            DriverStatus status);
}
```

---

## Delivery Service

```java
public interface DeliveryService {

    DeliveryTask createTask(
            CreateDeliveryTaskRequest request);

    void assignDriver(
            String taskId);

    void reassignDriver(
            String taskId);

    void markDelivered(
            String taskId);
}
```

---

# 8. Business Rules

### Assignment

```text
Only AVAILABLE drivers can receive tasks.
```

---

### Capacity

```text
Driver capacity cannot exceed limit.
```

---

### Delivery Completion

```text
Task must be PICKED_UP before DELIVERED.
```

---

### Reassignment

```text
Only non-delivered tasks can be reassigned.
```

---

# 9. State Transitions

## Delivery Lifecycle

```text
CREATED
    ↓
ASSIGNED
    ↓
PICKED_UP
    ↓
OUT_FOR_DELIVERY
    ↓
DELIVERED
```

---

## Failure Path

```text
OUT_FOR_DELIVERY
        ↓
FAILED
```

---

# 10. Design Patterns

## Strategy Pattern

Most important pattern in this design.

### Assignment Strategy

```java
public interface AssignmentStrategy {

    Driver assign(
            DeliveryTask task,
            List<Driver> drivers);
}
```

---

### Nearest Driver Strategy

```java
public class NearestDriverStrategy
        implements AssignmentStrategy {
}
```

---

### Least Loaded Strategy

```java
public class LeastLoadedStrategy
        implements AssignmentStrategy {
}
```

---

### Zone Based Strategy

```java
public class ZoneBasedStrategy
        implements AssignmentStrategy {
}
```

---

### Why Strategy?

Tomorrow business may say:

```text
Prioritize premium drivers.
```

Simply add:

```java
PremiumDriverStrategy
```

No modification to existing code.

---

# 11. Concurrency Challenges

## Scenario 1

Same driver assigned twice.

Current:

```text
Capacity = 1
```

Requests:

```text
Task A assignment

Task B assignment
```

arrive simultaneously.

---

### Solution

Optimistic Locking.

```java
private Long version;
```

---

Update:

```sql
UPDATE driver
SET assigned_tasks = assigned_tasks + 1
WHERE version = ?
```

---

## Scenario 2

Driver goes OFFLINE during assignment.

Example:

```text
Assignment Service reads AVAILABLE

Driver becomes OFFLINE

Assignment proceeds
```

---

### Solution

Revalidate before commit.

```text
Check status again
```

before final assignment.

---

# 12. Failure Handling

## Driver Does Not Accept Assignment

Flow:

```text
Assign
   ↓
Timeout
   ↓
Reassign
```

---

### Solution

Assignment timeout.

```text
30 sec
60 sec
```

---

## Driver App Offline

Solution:

```text
Heartbeat Monitoring
```

Example:

```text
No heartbeat for 2 mins
```

Mark:

```text
OFFLINE
```

---

## Delivery Failed

Example:

```text
Customer unavailable
```

Solution:

```text
Retry
Reschedule
Return to warehouse
```

---

# 13. Extensibility

## New Requirement

Vehicle Types.

Add:

```java
public enum VehicleType {

    BIKE,
    CAR,
    VAN
}
```

---

## New Requirement

Large Packages.

Add:

```java
maxWeight
```

inside Driver.

---

## New Requirement

Scheduled Delivery.

Add:

```java
deliveryWindow
```

inside DeliveryTask.

---

# 14. Auditability

## DeliveryEvent

```java
public class DeliveryEvent {

    private String eventId;

    private String taskId;

    private DeliveryStatus oldStatus;

    private DeliveryStatus newStatus;

    private Instant timestamp;
}
```

---

Examples:

```text
CREATED -> ASSIGNED

ASSIGNED -> PICKED_UP

PICKED_UP -> DELIVERED
```

---

# 15. Service Boundaries

## Driver Service

Owns:

```text
Drivers
Availability
Locations
```

---

## Assignment Service

Owns:

```text
Assignment Logic
Strategies
```

---

## Delivery Service

Owns:

```text
Task Lifecycle
```

---

## Tracking Service

Owns:

```text
Real-time Tracking
```

---

# 16. Database Tables

```sql
DRIVER
```

```sql
DELIVERY_TASK
```

```sql
ROUTE
```

```sql
DELIVERY_EVENT
```

```sql
DRIVER_LOCATION
```

---

# 17. Trade-Offs

## Calculate Nearest Driver On Demand

Pros:

* Freshest location

Cons:

* Expensive

---

## Maintain Nearby Driver Index

Pros:

* Faster assignment

Cons:

* Additional complexity

---

### Choice

At scale, maintain a geospatial index/grid for fast lookup.

---

# 18. Common Staff-Level Follow-Up Questions

### Q1. How would you find the nearest driver?

**Answer:**
Use geospatial indexing (GeoHash, grid-based partitioning, R-tree, etc.) instead of scanning all drivers.

---

### Q2. How would you handle delivery batching?

Example:

```text
10 packages
same apartment complex
```

**Answer:**
Group into a Route and assign together.

---

### Q3. How would you support same-day delivery?

**Answer:**
Prioritize assignment strategy based on SLA and delivery window.

---

### Q4. Driver accepts assignment but vehicle breaks down.

**Answer:**
Mark task for reassignment and publish a `DriverUnavailableEvent`.

---

### Q5. How would you optimize delivery costs?

**Answer:**
Batch deliveries, optimize routes, and minimize driver idle time.

---

### Q6. What metric matters most?

Typical answers:

```text
Assignment latency
Delivery success rate
Average delivery time
Driver utilization
```

---

# Staff-Level Discussion You Should Bring Up

Most candidates stop at:

```text
Driver
DeliveryTask
Assign()
```

A stronger discussion includes:

### Assignment Strategy

```text
Nearest
Least Loaded
Zone Based
SLA Based
```

### Capacity Constraints

```text
Weight
Volume
Task Count
```

### Reassignment

```text
Driver Offline
Driver Rejects
Vehicle Breakdown
```

### Route Optimization

```text
Single task
vs
Multi-stop route
```

### Real-Time Tracking

```text
Location updates
Heartbeat monitoring
```

These topics usually lead naturally into HLD discussions around fleet management, delivery tracking, and route optimization—areas that logistics-focused companies frequently explore with Staff-level candidates.

After this, the next high-value topic would be **Inventory Reservation System**, which is arguably the single most important LLD in e-commerce because it directly connects orders, warehouses, payments, and fulfillment. Even though we discussed it earlier, we can rebuild it using this Staff-level template and make it interview-ready.
