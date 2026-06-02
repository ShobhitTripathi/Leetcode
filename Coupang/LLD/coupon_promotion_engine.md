# 7. Coupon & Promotion Engine (Staff-Level LLD)

This is one of the most frequently asked LLDs because it tests:

* OOP fundamentals
* Open/Closed Principle
* Strategy Pattern
* Rule Evaluation
* Extensibility

Interviewers deliberately keep changing requirements to see whether your design survives.

---

# 1. Problem Statement

Design a Coupon & Promotion Engine for an e-commerce platform.

The system should support:

* Percentage discounts
* Flat discounts
* Buy X Get Y
* Free shipping
* Category-specific promotions
* User-specific promotions
* Multiple validation rules

---

# 2. Functional Requirements

### Coupon Management

* Create coupon
* Validate coupon
* Apply coupon
* Expire coupon

### Promotion Types

Support:

```text
10% OFF
₹500 OFF
Buy 2 Get 1
Free Shipping
```

---

### Rule Evaluation

Support conditions like:

```text
Minimum Order Value
Customer Segment
Product Category
Usage Limit
```

---

# 3. Non-Functional Requirements

* Extensible
* Configurable
* High performance
* Auditability
* Easy addition of new promotion types

---

# 4. Core Entities

```text
Coupon
    |
Promotion
    |
DiscountStrategy

Coupon
    |
ValidationRule
```

---

# 5. Basic Classes

## Cart

```java
public class Cart {

    private String customerId;

    private List<CartItem> items;

    private BigDecimal totalAmount;
}
```

---

## CartItem

```java
public class CartItem {

    private String productId;

    private int quantity;

    private BigDecimal price;
}
```

---

## Coupon

```java
public class Coupon {

    private String couponCode;

    private Promotion promotion;

    private List<ValidationRule> rules;

    private Instant startDate;

    private Instant endDate;
}
```

---

## DiscountResult

```java
public class DiscountResult {

    private BigDecimal discountAmount;

    private BigDecimal finalAmount;
}
```

---

# 6. Service APIs

## Coupon Service

```java
public interface CouponService {

    DiscountResult applyCoupon(
            String couponCode,
            Cart cart);

    boolean validateCoupon(
            String couponCode,
            Cart cart);
}
```

---

## Promotion Service

```java
public interface PromotionService {

    DiscountResult calculateDiscount(
            Cart cart,
            Promotion promotion);
}
```

---

# 7. Business Rules

### Coupon Expiry

```text
Expired coupons cannot be applied.
```

---

### Usage Limits

```text
One use per customer.
```

or

```text
1000 total uses.
```

---

### Minimum Order Value

```text
Minimum cart value ₹5000.
```

---

### Category Restrictions

```text
Applicable only on Electronics.
```

---

### Stacking Rules

```text
Can combine with other coupons?

Yes / No
```

---

# 8. Design Patterns

This is the most important section.

---

# Strategy Pattern

Every promotion type becomes a strategy.

## Discount Strategy

```java
public interface DiscountStrategy {

    DiscountResult apply(
            Cart cart);
}
```

---

## Percentage Discount

```java
public class PercentageDiscountStrategy
        implements DiscountStrategy {

    private int percentage;
}
```

---

## Flat Discount

```java
public class FlatDiscountStrategy
        implements DiscountStrategy {

    private BigDecimal amount;
}
```

---

## Free Shipping

```java
public class FreeShippingStrategy
        implements DiscountStrategy {
}
```

---

## Buy X Get Y

```java
public class BuyXGetYStrategy
        implements DiscountStrategy {
}
```

---

### Why?

Tomorrow business says:

```text
Buy 5 Get 2
```

Add:

```java
Buy5Get2Strategy
```

No existing code changes.

---

# Chain of Responsibility Pattern

Used for validations.

---

## Validation Rule

```java
public interface ValidationRule {

    ValidationResult validate(
            Cart cart,
            Coupon coupon);
}
```

---

## Minimum Order Rule

```java
public class MinimumOrderRule
        implements ValidationRule {
}
```

---

## Category Rule

```java
public class CategoryRule
        implements ValidationRule {
}
```

---

## Expiry Rule

```java
public class ExpiryRule
        implements ValidationRule {
}
```

---

## Usage Rule

```java
public class UsageRule
        implements ValidationRule {
}
```

---

Flow:

```text
Expiry Check
      ↓
Usage Check
      ↓
Category Check
      ↓
Minimum Order Check
```

---

# 9. State Transitions

## Coupon Lifecycle

```text
CREATED
     ↓
ACTIVE
     ↓
EXPIRED
```

---

or

```text
ACTIVE
    ↓
DISABLED
```

---

# 10. Concurrency Challenges

## Scenario 1

Limited Coupon

```text
First 100 customers only
```

Current:

```text
Remaining = 1
```

Two requests:

```text
Apply Coupon
Apply Coupon
```

simultaneously.

---

### Solution

Optimistic Locking.

```java
private Long version;
```

---

## Scenario 2

Same User Uses Coupon Twice

### Solution

Unique Constraint

```text
(customerId, couponCode)
```

---

# 11. Failure Handling

## Discount Applied but Order Creation Failed

Example:

```text
Coupon consumed
Order failed
```

---

### Solution

Reserve coupon usage.

```text
PENDING
```

Commit only after successful order creation.

---

## Coupon Service Down

Solution:

Graceful fallback.

```text
Proceed without discount
```

or

```text
Retry
```

based on business rules.

---

# 12. Extensibility

## New Requirement

Loyalty Discounts.

Add:

```java
LoyaltyDiscountStrategy
```

---

## New Requirement

Premium User Promotions.

Add:

```java
PremiumCustomerRule
```

---

## New Requirement

Festival Promotions.

Add:

```java
FestivalPromotionStrategy
```

---

## New Requirement

Tiered Discounts

Example:

```text
₹5000 → 5%

₹10000 → 10%

₹20000 → 15%
```

Add:

```java
TieredDiscountStrategy
```

---

# 13. Auditability

## CouponUsage

```java
public class CouponUsage {

    private String usageId;

    private String couponCode;

    private String customerId;

    private Instant usedAt;
}
```

---

Track:

```text
Who used coupon?
When?
How many times?
```

---

# 14. Service Boundaries

## Coupon Service

Owns:

```text
Coupon Validation
Coupon Lifecycle
```

---

## Promotion Service

Owns:

```text
Discount Calculation
```

---

## Order Service

Owns:

```text
Final Order Amount
```

---

## Customer Service

Owns:

```text
Customer Segments
```

---

# 15. Database Tables

```sql
COUPON
```

---

```sql
PROMOTION
```

---

```sql
COUPON_RULE
```

---

```sql
COUPON_USAGE
```

---

```sql
PROMOTION_AUDIT
```

---

# 16. Trade-Offs

## Hardcoded Rules

Pros:

```text
Simple
```

Cons:

```text
Deployment needed for every change
```

---

## Rule Engine

Pros:

```text
Business configurable
```

Cons:

```text
Higher complexity
```

---

### Typical Choice

Small company:

```text
Strategy Pattern
```

Large e-commerce:

```text
Rule Engine + Strategy Pattern
```

---

# 17. Common Staff-Level Follow-Up Questions

### Q1. How do you support adding new coupon types?

**Answer:**

Strategy Pattern.

---

### Q2. How do you support multiple validations?

**Answer:**

Chain of Responsibility.

---

### Q3. Can multiple coupons be applied?

**Answer:**

Introduce stacking rules and coupon priority.

---

### Q4. How do you prevent coupon abuse?

**Answer:**

Usage tracking + limits + customer restrictions.

---

### Q5. How would you support marketing teams creating promotions?

**Answer:**

Move rules into configurable metadata/rule engine instead of code.

---

### Q6. How do you handle "First 100 customers" promotions?

**Answer:**

Optimistic locking and atomic usage counters.

---

# Staff-Level Discussion You Should Bring Up

Most candidates design:

```text
Coupon
Promotion
```

and then write:

```java
if(type == PERCENTAGE)
```

```java
if(type == FLAT)
```

This is exactly what interviewers want to avoid.

A strong answer proactively discusses:

### Open/Closed Principle

```text
Add new promotions
without modifying old code
```

### Strategy Pattern

```text
Discount Calculation
```

### Rule Engine

```text
Validation Logic
```

### Coupon Abuse Prevention

```text
Usage Limits
Rate Limits
Customer Restrictions
```

### Coupon Reservation

```text
Apply
≠
Consume
```

Only consume after successful order creation.

### Auditability

```text
CouponUsage
PromotionAudit
```

---

Among the original list, the last remaining one is **Rate Limiter**, which is less domain-specific but one of the most common LLDs across Staff interviews because it tests concurrency, thread safety, data structures, and API design. It is also usually the most coding-heavy of the group.
