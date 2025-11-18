Here is a **proper, clean, production-quality Markdown file** you can directly use as a **README.md**.

---

# Authentication Manager — Scalable In-Memory Designs

This document explains multiple approaches to designing an efficient in-memory **Authentication Manager**, starting from the simplest implementation and evolving toward a highly scalable bucket-based system.

The goal is to support:

* Fast token generation
* Fast renewal
* Efficient expiration
* Scalable counting of unexpired tokens
* High throughput (hundreds of thousands to millions of requests)

This guide shows how each approach improves on the previous one and why the final bucket-based design is the most scalable in-memory solution.

---

## 1. Problem Overview

The system should maintain an authentication token store with the following behaviors:

1. **generate(tokenId, currentTime)**
   Creates a token that expires at `currentTime + timeToLive`.

2. **renew(tokenId, currentTime)**
   Extends expiry if the token is unexpired.

3. **countUnexpiredTokens(currentTime)**
   Returns number of active tokens.

4. Expiration at time `t` occurs **before** any other action at time `t`.

Key constraint:
`currentTime` is strictly increasing.

---

## 2. Approach 1 — Basic HashMap with Full Cleanup

### Description

A simple method:

* Use a HashMap mapping tokenId → expiryTime.
* On each operation, remove expired entries by scanning the entire map.

### Pros

* Easiest to implement.
* Works for small datasets (<10k tokens).

### Cons

* Cleanup is O(n) every time.
* Causes latency spikes.
* Cannot scale to large token counts.

### Use When

* Very small scale or purely educational purposes.

---

## 3. Approach 2 — Lazy Cleanup with Threshold

### Description

Cleanup only happens:

* When many tokens accumulate, or
* When time advances sufficiently.

This reduces the frequency of full scans.

### Pros

* More efficient than Approach 1.
* Cleanup does not occur on every call.

### Cons

* Still O(n) in the worst case.
* Memory can grow between cleanups.

### Use When

* Moderate workloads (10k–100k tokens).
* Occasional latency spikes are acceptable.

---

## 4. Approach 3 — Priority Queue + HashMap

### Description

Uses:

* HashMap: tokenId → latest expiryTime
* Min-heap (PriorityQueue): (expiryTime, tokenId)

Expired tokens are removed via:

* Pop from heap while top entry is expired.
* Remove only if heap's expiry matches the HashMap's expiry (lazy deletion).

### Pros

* Cleanup cost proportional to expired tokens only.
* No full-map scans.
* Works well for hundreds of thousands to a few million tokens.

### Cons

* generate/renew operations are O(log n).
* Heap grows larger due to stale entries.
* Requires careful lazy cleanup.

### Use When

* You need predictable performance.
* High volume systems up to a few million tokens.

---

## 5. Approach 4 — Time-Based Bucketing (Recommended)

### Description

Group tokens by their expiryTime:

```
expiryTime → { tokenIds }
```

Use a TreeMap to access the earliest expiry bucket. Cleanup removes whole buckets at once.

This design is inspired by timing wheels used in:

* Netty
* Kafka
* Linux kernel schedulers

### Pros

* Very fast cleanup: remove whole buckets.
* Nearly O(1) generate and renew.
* TreeMap contains only active expiry timestamps.
* No stale entries like PQ-based methods.
* Ideal for predictable TTL workloads.

### Cons

* Slightly more complex than PQ approach.
* TreeMap still has O(log n) overhead, though number of buckets is small.

### Use When

* You need to support millions of active tokens.
* Low-latency predictable operations are required.
* You want the fastest in-memory design.

---

## 6. Final In-Memory Bucket-Based Implementation

```java
import java.util.*;

class AuthenticationManager {

    private final int timeToLive;
    private final Map<String, Integer> tokenExpiryMap;          // tokenId -> expiryTime
    private final TreeMap<Integer, Set<String>> expiryBuckets;  // expiryTime -> tokens

    public AuthenticationManager(int timeToLive) {
        this.timeToLive = timeToLive;
        this.tokenExpiryMap = new HashMap<>();
        this.expiryBuckets = new TreeMap<>();
    }

    public void generate(String tokenId, int currentTime) {
        cleanup(currentTime);

        int expiry = currentTime + timeToLive;
        tokenExpiryMap.put(tokenId, expiry);

        expiryBuckets
            .computeIfAbsent(expiry, k -> new HashSet<>())
            .add(tokenId);
    }

    public void renew(String tokenId, int currentTime) {
        cleanup(currentTime);

        Integer oldExpiry = tokenExpiryMap.get(tokenId);
        if (oldExpiry == null || oldExpiry <= currentTime) {
            return;
        }

        int newExpiry = currentTime + timeToLive;

        // Remove from old bucket
        Set<String> oldBucket = expiryBuckets.get(oldExpiry);
        oldBucket.remove(tokenId);
        if (oldBucket.isEmpty()) {
            expiryBuckets.remove(oldExpiry);
        }

        // Add to new bucket
        tokenExpiryMap.put(tokenId, newExpiry);
        expiryBuckets
            .computeIfAbsent(newExpiry, k -> new HashSet<>())
            .add(tokenId);
    }

    public int countUnexpiredTokens(int currentTime) {
        cleanup(currentTime);
        return tokenExpiryMap.size();
    }

    private void cleanup(int currentTime) {
        while (!expiryBuckets.isEmpty()) {

            Map.Entry<Integer, Set<String>> entry = expiryBuckets.firstEntry();
            int bucketExpiry = entry.getKey();

            if (bucketExpiry > currentTime) break;

            Set<String> tokens = expiryBuckets.pollFirstEntry().getValue();

            for (String tokenId : tokens) {
                Integer expiry = tokenExpiryMap.get(tokenId);
                if (expiry != null && expiry <= currentTime) {
                    tokenExpiryMap.remove(tokenId);
                }
            }
        }
    }
}
```

---

## 7. Comparison Table

| Approach          | Cleanup Complexity  | Per-Op Cost | Memory Behavior    | Suitable Scale  |
| ----------------- | ------------------- | ----------- | ------------------ | --------------- |
| HashMap Full Scan | O(n)                | O(1)        | Clean              | < 10k tokens    |
| Lazy Cleanup      | O(n) occasionally   | O(1)        | Moderate memory    | 10k–100k tokens |
| PQ + HashMap      | O(k log n)          | O(log n)    | Stale heap entries | 100k–3M tokens  |
| Bucket-Based      | O(buckets + tokens) | O(1) avg    | Very efficient     | 1M+ tokens      |

`k = number of expired tokens`, `n = active tokens`.

---

## 8. Why Bucket-Based is the Final Choice

1. Removes entire expiry buckets at once.
2. Avoids O(log n) heap operations.
3. Keeps only active buckets in the TreeMap.
4. Clean, predictable, high-performance behavior.
5. Mimics designs used in high-performance networking frameworks.

This results in the most scalable and efficient single-node, in-memory token manager.

---

## 9. Future Extensions

For distributed scale:

* Redis Sorted Sets (ZSET)
* Clustered caches (Redis Cluster, Hazelcast, Ignite)
* Microservice architecture with shared token store

But for a single in-memory implementation, the bucket-based architecture is optimal.

---

If you'd like, I can also prepare:

* A repo-ready folder structure
* A diagram for each approach
* A sequence diagram for generate/renew/cleanup
* A performance benchmark summary

Just let me know.
