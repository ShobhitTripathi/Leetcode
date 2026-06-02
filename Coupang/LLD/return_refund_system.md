# 6. Return & Refund System (Staff-Level LLD)

This is one of the most realistic e-commerce LLDs because it touches multiple domains:

```text
Order
Inventory
Warehouse
Payment
Shipment
Customer Experience
```

For companies like Coupang, returns are a huge operational cost center, so interviewers often care about business rules and workflow correctness.

---

# 1. Problem Statement

Design a Return & Refund System that supports:

* Return requests
* Return approval/rejection
* Pickup scheduling
* Product inspection
* Refund processing
* Partial returns
* Partial refunds

---

# 2. Functional Requirements

### Return Management

* Create return request
* Approve/reject return
* Track return status
* Support partial returns

### Pickup Management

* Schedule pickup
* Track pickup status

### Inspection

* Inspect returned item
* Approve/reject refund

### Refund

* Process refund
* Track refund status

---

# 3. Non-Functional Requirements

* Auditability
* Idempotency
* Extensible return policies
* High reliability
* Complete refund traceability

---

# 4. Core Entities

```text
Order
   |
OrderItem
   |
ReturnRequest
   |
ReturnItem
   |
Inspection
   |
Refund
```

---

# 5. Basic Classes

## ReturnRequest

```java
public class ReturnRequest {

    private String returnId;

    private String orderId;

    private String customerId;

    private ReturnStatus status;

    private Instant createdAt;
}
```

---

## ReturnItem

Supports partial returns.

```java
public class ReturnItem {

    private String itemId;

    private String productId;

    private int quantity;

    private ReturnReason reason;
}
```

---

## Pickup

```java
public class Pickup {

    private String pickupId;

    private String returnId;

    private PickupStatus status;

    private Instant scheduledDate;
}
```

---

## Inspection

```java
public class Inspection {

    private String inspectionId;

    private String returnId;

    private InspectionResult result;

    private String remarks;
}
```

---

## Refund

```java
public class Refund {

    private String refundId;

    private String returnId;

    private BigDecimal amount;

    private RefundStatus status;
}
```

---

# 6. Supporting Enums

## Return Status

```java
public enum ReturnStatus {

    REQUESTED,

    APPROVED,

    REJECTED,

    PICKUP_SCHEDULED,

    PICKED_UP,

    INSPECTED,

    REFUNDED,

    CLOSED
}
```

---

## Refund Status

```java
public enum RefundStatus {

    PENDING,

    PROCESSING,

    SUCCESS,

    FAILED
}
```

---

## Inspection Result

```java
public enum InspectionResult {

    PASSED,

    FAILED
}
```

---

# 7. Service APIs

## Return Service

```java
public interface ReturnService {

    ReturnRequest createReturn(
            CreateReturnRequest request);

    void approveReturn(
            String returnId);

    void rejectReturn(
            String returnId);

    ReturnRequest getReturn(
            String returnId);
}
```

---

## Pickup Service

```java
public interface PickupService {

    Pickup schedulePickup(
            String returnId);

    void markPickedUp(
            String pickupId);
}
```

---

## Inspection Service

```java
public interface InspectionService {

    void inspect(
            String returnId,
            InspectionResult result);
}
```

---

## Refund Service

```java
public interface RefundService {

    Refund initiateRefund(
            String returnId);

    void completeRefund(
            String refundId);
}
```

---

# 8. Business Rules

### Return Window

```text
Return allowed within 30 days.
```

---

### Delivery Requirement

```text
Only delivered orders can be returned.
```

---

### Refund Eligibility

```text
Refund only after successful inspection.
```

---

### Partial Returns

```text
Customer can return subset of items.
```

---

### Refund Limit

```text
Refund amount cannot exceed paid amount.
```

---

# 9. State Transitions

## Return Lifecycle

```text
REQUESTED
     ↓
APPROVED
     ↓
PICKUP_SCHEDULED
     ↓
PICKED_UP
     ↓
INSPECTED
     ↓
REFUNDED
     ↓
CLOSED
```

---

## Rejection Flow

```text
REQUESTED
      ↓
REJECTED
      ↓
CLOSED
```

---

## Failed Inspection Flow

```text
INSPECTED
     ↓
FAILED
     ↓
RETURN_REJECTED
```

---

# 10. Design Patterns

## State Pattern

Perfect fit.

```java
public interface ReturnState {

    void approve();

    void reject();

    void refund();
}
```

Implementations:

```java
RequestedState
ApprovedState
InspectedState
RefundedState
```

---

## Strategy Pattern

### Return Policy

```java
public interface ReturnPolicy {

    boolean isEligible(
            Order order,
            ReturnRequest request);
}
```

Implementations:

```java
ThirtyDayReturnPolicy

PremiumCustomerPolicy

ElectronicsPolicy
```

---

Why?

Tomorrow business says:

```text
Electronics -> 7 days

Fashion -> 30 days

Furniture -> No returns
```

No redesign needed.

---

# 11. Concurrency Challenges

## Scenario 1

Customer submits same return twice.

### Problem

```text
Return Request A

Return Request B
```

for same item.

---

### Solution

Idempotency.

Validate:

```text
orderItemId
```

already under return.

---

## Scenario 2

Refund triggered twice.

Example:

```text
Retry
Network Failure
Duplicate Event
```

---

### Solution

Refund idempotency key.

```java
returnId
```

must generate only one refund.

---

# 12. Failure Handling

## Refund Gateway Success But DB Update Fails

Example:

```text
Money returned

Refund table not updated
```

---

### Solution

Saga Pattern.

```text
RefundInitiatedEvent
```

with retries.

---

## Pickup Service Down

Solution:

```text
Return approved
```

but

```text
Pickup scheduling async
```

using events.

---

## Inspection System Down

Solution:

Queue inspection requests and process later.

---

# 13. Extensibility

## New Requirement

Instant Refund.

Current flow:

```text
Pickup
 ↓
Inspection
 ↓
Refund
```

New flow:

```text
Pickup
 ↓
Refund
```

for trusted customers.

Add:

```java
RefundPolicy
```

---

## New Requirement

Store Credit Refund.

Add:

```java
RefundMethod
```

```java
BANK

ORIGINAL_PAYMENT

STORE_CREDIT
```

---

## New Requirement

Exchange Product Instead of Refund.

Add:

```java
ExchangeRequest
```

without impacting existing return flow.

---

# 14. Auditability

Critical in financial workflows.

## ReturnEvent

```java
public class ReturnEvent {

    private String eventId;

    private String returnId;

    private ReturnStatus oldStatus;

    private ReturnStatus newStatus;

    private Instant timestamp;
}
```

---

## RefundEvent

```java
public class RefundEvent {

    private String refundId;

    private String returnId;

    private BigDecimal amount;

    private Instant timestamp;
}
```

---

# 15. Service Boundaries

## Order Service

Owns:

```text
Order
OrderItem
```

---

## Return Service

Owns:

```text
Return Requests
```

---

## Pickup Service

Owns:

```text
Pickup Scheduling
```

---

## Inspection Service

Owns:

```text
Quality Check
```

---

## Refund Service

Owns:

```text
Money Movement
```

---

## Inventory Service

Owns:

```text
Restocking Returned Items
```

---

# 16. Database Tables

```sql
RETURN_REQUEST
```

```sql
RETURN_ITEM
```

```sql
PICKUP
```

```sql
INSPECTION
```

```sql
REFUND
```

```sql
RETURN_EVENT
```

```sql
REFUND_EVENT
```

---

# 17. Trade-Offs

## Refund Before Inspection

Pros:

```text
Great customer experience
```

Cons:

```text
Fraud risk
```

---

## Refund After Inspection

Pros:

```text
Lower fraud
```

Cons:

```text
Slower refund
```

---

### Typical Choice

Trusted customers:

```text
Instant Refund
```

Others:

```text
Inspection First
```

---

# 18. Common Staff-Level Follow-Up Questions

### Q1. How do you support partial returns?

**Answer:**

Return is tracked at `ReturnItem` level, not just `ReturnRequest` level.

---

### Q2. How do you prevent duplicate refunds?

**Answer:**

Idempotency using `returnId` and refund transaction records.

---

### Q3. What happens to returned inventory?

**Answer:**

After inspection:

```text
Good Condition
      ↓
Restock

Damaged
      ↓
Dispose / Refurbish
```

---

### Q4. How do you support different return policies?

**Answer:**

Strategy Pattern (`ReturnPolicy`).

---

### Q5. How would you handle pickup failures?

**Answer:**

Retry scheduling, allow reschedule, maintain pickup status lifecycle.

---

### Q6. Payment gateway says refund succeeded but our system crashed.

**Answer:**

Saga pattern + reconciliation jobs + idempotent refund processing.

---

# Staff-Level Discussion You Should Bring Up

Most candidates stop at:

```text
ReturnRequest
Refund
```

A stronger answer proactively discusses:

### Financial Consistency

```text
Refund Idempotency
Reconciliation Jobs
```

### Inventory Impact

```text
Restock
Damaged Inventory
```

### Fraud Prevention

```text
Inspection
Customer Trust Score
```

### Policy Engine

```text
Different return windows
Different product categories
```

### Event History

```text
ReturnEvent
RefundEvent
```

### Service Ownership

```text
Returns ≠ Refunds
```

They are separate domains and often separate services.

---

After this, the next one is **Coupon & Promotion Engine**, which is one of the most pattern-heavy LLDs and is frequently used to evaluate extensibility, OCP, Strategy, and rule-engine thinking.
