# Authentication Manager — Scalable In-Memory Implementations

### A Study of Approaches from Basic to Production-Grade Solutions

This document describes the evolution of solutions for the **Authentication Manager** problem (LeetCode 1797).
We start from the simplest HashMap approach and progress toward a highly scalable **bucket-based design** suitable for millions of tokens.

Each section includes:

* Concept summary
* Pros & cons
* Time complexity
* Full Java implementation

---

# 1. Problem Overview

We need to build an authentication token manager that supports:

* `generate(tokenId, currentTime)`
* `renew(tokenId, currentTime)`
* `countUnexpiredTokens(currentTime)`

Rules:

* Each token expires at `currentTime + timeToLive`
* A token expiring at time `t` is considered expired for any operation at `t`
* `currentTime` is strictly increasing
* Up to 2000 calls (LeetCode), but we aim for **massive-scale** in-memory designs

---

# 2. Approach 1 — Basic HashMap (Full Scan Cleanup)

### Idea

Store all tokens in a HashMap.
On every operation, scan the entire map and remove expired tokens.

### Pros

* Very easy to implement

### Cons

* Cleanup cost is O(n) every time
* Not scalable for large datasets

### Time Complexity

| Operation            | Time                |
| -------------------- | ------------------- |
| generate             | O(n) due to cleanup |
| renew                | O(n)                |
| countUnexpiredTokens | O(n)                |
| cleanup              | O(n)                |

### Java Implementation

```java
class AuthenticationManager {

    private final int timeToLive;
    private final Map<String, Integer> tokenMap;

    public AuthenticationManager(int timeToLive) {
        this.timeToLive = timeToLive;
        this.tokenMap = new HashMap<>();
    }

    public void generate(String tokenId, int currentTime) {
        cleanup(currentTime);
        tokenMap.put(tokenId, currentTime + timeToLive);
    }

    public void renew(String tokenId, int currentTime) {
        cleanup(currentTime);
        Integer expiry = tokenMap.get(tokenId);
        if (expiry != null && expiry > currentTime) {
            tokenMap.put(tokenId, currentTime + timeToLive);
        }
    }

    public int countUnexpiredTokens(int currentTime) {
        cleanup(currentTime);
        return tokenMap.size();
    }

    private void cleanup(int currentTime) {
        Iterator<Map.Entry<String, Integer>> it = tokenMap.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() <= currentTime) {
                it.remove();
            }
        }
    }
}
```

---

# 3. Approach 2 — HashMap with Lazy Cleanup

### Idea

Cleanup is done periodically instead of every operation.

### Pros

* Avoids constant scanning
* Good for mid-scale workloads

### Cons

* Still O(n) cleanup
* Memory grows between cleanups

### Time Complexity

| Operation | Time              |
| --------- | ----------------- |
| generate  | O(1) avg          |
| renew     | O(1) avg          |
| count     | O(1) avg          |
| cleanup   | O(n) occasionally |

### Java Implementation

```java
class AuthenticationManager {

    private final int timeToLive;
    private final Map<String, Integer> tokenMap;
    private int operations = 0;
    private final int CLEANUP_THRESHOLD = 1000;

    public AuthenticationManager(int timeToLive) {
        this.timeToLive = timeToLive;
        this.tokenMap = new HashMap<>();
    }

    public void generate(String tokenId, int currentTime) {
        lazyCleanup(currentTime);
        tokenMap.put(tokenId, currentTime + timeToLive);
    }

    public void renew(String tokenId, int currentTime) {
        lazyCleanup(currentTime);
        Integer expiry = tokenMap.get(tokenId);
        if (expiry != null && expiry > currentTime) {
            tokenMap.put(tokenId, currentTime + timeToLive);
        }
    }

    public int countUnexpiredTokens(int currentTime) {
        lazyCleanup(currentTime);
        return tokenMap.size();
    }

    private void lazyCleanup(int currentTime) {
        operations++;
        if (operations % CLEANUP_THRESHOLD != 0) return;

        Iterator<Map.Entry<String, Integer>> it = tokenMap.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() <= currentTime) {
                it.remove();
            }
        }
    }
}
```

---

# 4. Approach 3 — PriorityQueue + HashMap

### Idea

Use:

* HashMap for latest expiry
* Min-heap for earliest expirations

Expired tokens are removed by repeatedly popping from the heap.

### Pros

* Cleanup is efficient
* Only expired entries are removed
* No full scans

### Cons

* Heap contains stale entries (fixed by lazy validation)
* generate/renew are O(log n)

### Time Complexity

| Operation | Time                            |
| --------- | ------------------------------- |
| generate  | O(log n)                        |
| renew     | O(log n)                        |
| count     | O(log n)                        |
| cleanup   | O(k log n) (k = expired tokens) |

### Java Implementation

```java
import java.util.*;

class AuthenticationManager {

    private final int timeToLive;
    private final Map<String, Integer> tokenExpiry;
    private final PriorityQueue<int[]> pq;

    public AuthenticationManager(int timeToLive) {
        this.timeToLive = timeToLive;
        this.tokenExpiry = new HashMap<>();
        this.pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
    }

    public void generate(String tokenId, int currentTime) {
        cleanup(currentTime);
        int expiry = currentTime + timeToLive;
        tokenExpiry.put(tokenId, expiry);
        pq.offer(new int[]{tokenId.hashCode(), expiry});
    }

    public void renew(String tokenId, int currentTime) {
        cleanup(currentTime);
        Integer oldExpiry = tokenExpiry.get(tokenId);
        if (oldExpiry != null && oldExpiry > currentTime) {
            int newExpiry = currentTime + timeToLive;
            tokenExpiry.put(tokenId, newExpiry);
            pq.offer(new int[]{tokenId.hashCode(), newExpiry});
        }
    }

    public int countUnexpiredTokens(int currentTime) {
        cleanup(currentTime);
        return tokenExpiry.size();
    }

    private void cleanup(int currentTime) {
        while (!pq.isEmpty() && pq.peek()[1] <= currentTime) {
            int[] top = pq.poll();
            int expiry = top[1];
            int hash = top[0];
            tokenExpiry.entrySet().removeIf(e -> e.getKey().hashCode() == hash && e.getValue() == expiry);
        }
    }
}
```

---

# 5. Approach 4 — Time-Based Bucketing (Timing Wheel Style)

### Idea

Group tokens by expiry time:

```
expiryTime → {tokenIds}
```

A TreeMap stores buckets in sorted order of expiry.

Cleanup:

* Remove entire bucket where expiryTime ≤ currentTime
* Remove corresponding entries from token map

### Pros

* Cleanup is extremely fast
* Removes large batches in O(1) per bucket
* No heap, no stale entries
* Scales to millions of tokens easily

### Cons

* Slightly more complex logic
* TreeMap lookup is O(log n), but number of buckets is typically small

### Time Complexity

| Operation | Time                                |
| --------- | ----------------------------------- |
| generate  | O(1) avg                            |
| renew     | O(1) avg                            |
| count     | O(1) avg                            |
| cleanup   | O(expired buckets + tokens in them) |

### Final Recommended Java Implementation

```java
import java.util.*;

class AuthenticationManager {

    private final int timeToLive;
    private final Map<String, Integer> tokenExpiryMap;
    private final TreeMap<Integer, Set<String>> expiryBuckets;

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

        Set<String> oldBucket = expiryBuckets.get(oldExpiry);
        oldBucket.remove(tokenId);
        if (oldBucket.isEmpty()) expiryBuckets.remove(oldExpiry);

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

# 6. Summary of Approaches

| Approach            | Scale      | Pros                 | Cons                    |
| ------------------- | ---------- | -------------------- | ----------------------- |
| HashMap (Full Scan) | Small      | Simple               | O(n) cleanup            |
| Lazy Cleanup        | Medium     | Reduced scans        | Still O(n) worst-case   |
| PQ + HashMap        | Large      | Efficient cleanup    | O(log n), stale entries |
| Bucket-Based        | Very Large | Fastest, predictable | More complex            |

---

# 7. Final Recommendation

For real-world systems that may handle millions of tokens, the **bucket-based approach** is the most efficient and scalable in-memory solution.
It minimizes latency, avoids log(n) heap operations, and provides predictable cleanup behavior.


Just let me know.
