# Low-Level Design: Rate Limiter

## **Problem Statement**
Design a rate limiter that:
- Allows a defined number of requests within a specific time window.
- Can be applied per user (or client).
- Handles concurrency and scalability concerns.

### **Example**
"Design a rate limiter that allows 10 requests per second per user. Occasional bursts should be allowed but averaged over a second."

---

## **Approach**

### **Step 1: Clarify Requirements**
- **Global vs User-Specific Limits**: Per-user rate limiting.
- **Burst Traffic**: Occasional bursts allowed (suggest Token Bucket for this).
- **Distributed or Single-Node**: This design is for single-node systems.
- **Action on Limit Exceeding**: Requests exceeding the limit are denied.

### **Step 2: Strategies for Rate Limiting**
- **Fixed Window**:
  - Simple to implement.
  - Inaccurate near boundary conditions.
- **Sliding Window**:
  - Tracks timestamps, more accurate.
  - Requires more memory.
- **Token Bucket**:
  - Allows bursts.
  - Slightly more complex.
- **Leaky Bucket**:
  - Smoothens traffic but doesn’t allow bursts.

### **Chosen Strategy**: Sliding Window for accuracy.

---

## **Core Components**
1. **RateLimiter Interface**:
   - Common interface for all rate limiter implementations.
   ```java
   public interface RateLimiter {
       boolean allowRequest(String clientId);
   }
   ```

2. **SlidingWindowRateLimiter Class**:
   - Implements rate limiting using sliding window logic.
   - Tracks request timestamps for each client using a `HashMap`.

3. **Data Structures**:
   - `HashMap<String, Queue<Long>>`: Stores client IDs and their request timestamps.
   - `Queue<Long>`: Keeps track of request timestamps.

4. **Concurrency Handling**:
   - `synchronized` methods for thread safety.

---

## **Sliding Window Implementation**
```java
import java.util.*;

public class SlidingWindowRateLimiter implements RateLimiter {
    private final int maxRequests; // Maximum requests per window
    private final long windowSizeMillis; // Time window in milliseconds
    private final Map<String, Queue<Long>> requestTimestamps; // User request map

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.requestTimestamps = new HashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();

        // Initialize queue for client if not present
        requestTimestamps.putIfAbsent(clientId, new LinkedList<>());
        Queue<Long> timestamps = requestTimestamps.get(clientId);

        // Remove expired timestamps
        while (!timestamps.isEmpty() && currentTime - timestamps.peek() > windowSizeMillis) {
            timestamps.poll();
        }

        // Check if request is within limit
        if (timestamps.size() < maxRequests) {
            timestamps.add(currentTime);
            return true;
        }
        return false;
    }
}
```

---

## **Alternate Rate Limiting Strategies**

### **1. Fixed Window Rate Limiter**
```java
import java.util.*;

public class FixedWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final Map<String, Integer> requestCounts;
    private final Map<String, Long> windowStartTimes;

    public FixedWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.requestCounts = new HashMap<>();
        this.windowStartTimes = new HashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();
        windowStartTimes.putIfAbsent(clientId, currentTime);
        requestCounts.putIfAbsent(clientId, 0);

        long windowStart = windowStartTimes.get(clientId);
        if (currentTime - windowStart >= windowSizeMillis) {
            windowStartTimes.put(clientId, currentTime);
            requestCounts.put(clientId, 0);
        }

        int count = requestCounts.get(clientId);
        if (count < maxRequests) {
            requestCounts.put(clientId, count + 1);
            return true;
        }
        return false;
    }
}
```

### **2. Token Bucket Rate Limiter**
```java
import java.util.*;

public class TokenBucketRateLimiter implements RateLimiter {
    private final int capacity;
    private final int refillRate; // Tokens per second
    private final Map<String, Integer> tokenBuckets;
    private final Map<String, Long> lastRefillTimes;

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokenBuckets = new HashMap<>();
        this.lastRefillTimes = new HashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();
        tokenBuckets.putIfAbsent(clientId, capacity);
        lastRefillTimes.putIfAbsent(clientId, currentTime);

        // Refill tokens
        long lastRefillTime = lastRefillTimes.get(clientId);
        int tokensToAdd = (int) ((currentTime - lastRefillTime) / 1000 * refillRate);
        if (tokensToAdd > 0) {
            int newTokens = Math.min(capacity, tokenBuckets.get(clientId) + tokensToAdd);
            tokenBuckets.put(clientId, newTokens);
            lastRefillTimes.put(clientId, currentTime);
        }

        // Allow or deny request
        int availableTokens = tokenBuckets.get(clientId);
        if (availableTokens > 0) {
            tokenBuckets.put(clientId, availableTokens - 1);
            return true;
        }
        return false;
    }
}
```

---

## **Key Considerations**

### **Concurrency**
- Use `synchronized` or `ConcurrentHashMap` for thread safety.
- Trade-offs: Synchronization can cause bottlenecks under high load.

### **Memory Usage**
- Sliding Window uses more memory for storing timestamps.
- Periodically clean up stale entries from `HashMap` to prevent memory leaks.

### **Scalability**
- This design works for a single-node setup.
- For distributed systems, extend using sharding or consistent hashing.

### **Accuracy vs Complexity**
- Sliding Window offers higher accuracy than Fixed Window but requires more computation and memory.

---

## **Trade-offs Summary**
| Strategy         | Pros                               | Cons                                    |
|------------------|------------------------------------|-----------------------------------------|
| Fixed Window     | Simple to implement               | Boundary inaccuracies                  |
| Sliding Window   | Accurate and flexible             | Higher memory usage, slightly complex  |
| Token Bucket     | Supports bursts                   | Needs periodic refill logic            |
| Leaky Bucket     | Smoothens traffic                 | Doesn’t allow bursts                   |

---

## **Conclusion**
The **Sliding Window Rate Limiter** is a good choice for accurate rate limiting in a single-node setup. It tracks request timestamps for each user and ensures fair usage of resources.

For more advanced setups, consider extending the design to:
- Use distributed stores like Redis for scalability.
- Add monitoring and logging for better observability.

