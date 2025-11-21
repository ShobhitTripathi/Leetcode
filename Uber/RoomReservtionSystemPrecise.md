
# **Optimized Solution (Using TreeMap)**

### Key Observations

1. Each room’s schedule is a set of **non-overlapping intervals**.
2. The ONLY operations we need per room:

   * Find the **floor interval** (immediately before the requested start time)
   * Find the **ceiling interval** (immediately after the requested start time)

Both can be done in **O(log K)** using a `TreeMap`, where:

```
Key     = meeting startTime
Value   = meeting endTime
```

### Why TreeMap?

* `floorEntry(key)` → previous meeting
* `ceilingEntry(key)` → next meeting
* `put(key, value)` → insert new meeting
  All operations are **logarithmic**.

### Benefits

* No shifting, no O(K) insertions
* Per room complexity: **O(log K)**
* Overall scheduling complexity: **O(R log K)**
  For fixed R (100 rooms), this is extremely efficient.

---

# **Optimized Code (Java — TreeMap per room)**

```java
import java.util.*;

class MeetingScheduler {

    private Map<String, TreeMap<Integer, Integer>> rooms;

    public MeetingScheduler(List<String> roomIds) {
        rooms = new HashMap<>();
        for (String room : roomIds) {
            rooms.put(room, new TreeMap<>());
        }
    }

    public String scheduleMeeting(int start, int end) {
        if (start >= end)
            throw new IllegalArgumentException("Invalid interval");

        for (String room : rooms.keySet()) {
            TreeMap<Integer, Integer> schedule = rooms.get(room);

            // Meeting just before requested interval
            Map.Entry<Integer, Integer> floor = schedule.floorEntry(start);

            // Meeting just after requested interval
            Map.Entry<Integer, Integer> ceil = schedule.ceilingEntry(start);

            // Check for overlap with the previous meeting
            if (floor != null && floor.getValue() > start)
                continue;

            // Check for overlap with the next meeting
            if (ceil != null && ceil.getKey() < end)
                continue;

            // No overlap — book it
            schedule.put(start, end);
            return room;
        }

        throw new RuntimeException("No rooms available");
    }
}
```

---

# **Explanation**

### For each room:

Let intervals be:

```
(start1, end1), (start2, end2), ...
```

When booking `(start, end)`:

1. `floor = schedule.floorEntry(start)`
   This is the interval that ends **just before** the new meeting might start.

2. `ceil = schedule.ceilingEntry(start)`
   This is the interval that starts **right after** the new meeting might start.

To check for conflicts:

```
floor.end > start   → overlaps
ceil.start < end    → overlaps
```

If both checks pass, you insert and return that room.

---

# **Complexity Analysis**

Let:

* **R** = number of rooms
* **K** = average meetings per room

### Per room:

```
floorEntry:  O(log K)
ceilingEntry: O(log K)
put:          O(log K)
---------------------------------
Total per room: O(log K)
```

### Overall:

You may scan all rooms in worst case:

```
O(R log K)
```

### Space:

```
O(R * K)
```

---

# **Why This Is Truly Optimized**

Compared to previous solution:

| Feature                  | Old Approach     | Optimized Approach     |
| ------------------------ | ---------------- | ---------------------- |
| Per room search + insert | O(K)             | O(log K)               |
| Data structure           | ArrayList        | TreeMap (Balanced BST) |
| Overall scheduling       | O(R * K)         | O(R * log K)           |
| Scalability              | Poor for large K | Excellent              |

This is the correct optimized answer expected in system design / DSA interviews.

---

