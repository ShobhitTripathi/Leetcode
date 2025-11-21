# 1. Problem Restatement

You need to build a **conference room reservation system**.

* You are given a fixed list of room IDs (e.g., 100 rooms: `["roomA", "roomB", ..., "roomZ"]`).
* You must support scheduling meetings:

```
scheduleMeeting(startTime, endTime) → reservationId OR error
```

Rules:

* A meeting can only be scheduled if **at least one room** is free for that time interval.
* Schedule should return:

  * A generated reservation ID
  * The assigned roomId
* If **no rooms** are free → return error.
* Assume meetings are **1D intervals** on a time axis (start, end).
* Assume rooms = 100 (fixed), but later we want to scale to arbitrary N.

---

# 2. Approach

### Key Idea:

Maintain **per-room sorted bookings** and check availability room-by-room.

For each room, store its meetings as **sorted non-overlapping intervals**:

```
roomA :  (1,3), (5,7), (10,12)
roomB :  (2,4), (6,8)
...
```

To schedule a new meeting `(s,e)`:

* For each room:

  * Use binary search to check if interval overlaps with existing bookings.
  * If no overlap → assign this room and insert the interval.
* If all rooms are checked and none are available → return error.

---

# 3. Steps to Solve

### Step 1: Maintain a map

```
Map<roomId, List<Interval>>
```

Where each list is **sorted by start time**.

### Step 2: For each call to scheduleMeeting(start, end)

1. Iterate through rooms (100 rooms is small).
2. For each room:

   * Perform binary search on its sorted interval list.
   * Check overlap with neighbors.
   * If no conflict:

     * Insert the interval in sorted order.
     * Build and return reservationId: `"roomA-<uuid>"`.
3. If no rooms free → return error.

### Step 3: Overlap Rule

Meeting `(s1, e1)` conflicts with `(s2, e2)` if:

```
not(e1 <= s2 or e2 <= s1)
```

---

# 4. Best Implementation (Clean Java Code)

```java
import java.util.*;

class ReservationSystem {

    static class Interval {
        int start, end;
        Interval(int s, int e) {
            start = s; end = e;
        }
    }

    private Map<String, List<Interval>> roomBookings;
    private List<String> rooms;

    public ReservationSystem(List<String> roomIds) {
        this.rooms = roomIds;
        this.roomBookings = new HashMap<>();
        for (String r : rooms) {
            roomBookings.put(r, new ArrayList<>());
        }
    }

    public String scheduleMeeting(int start, int end) {
        if (start >= end) {
            throw new IllegalArgumentException("Invalid time interval");
        }

        for (String room : rooms) {
            List<Interval> bookings = roomBookings.get(room);

            // binary search to find insertion position
            int pos = Collections.binarySearch(
                bookings,
                new Interval(start, end),
                Comparator.comparingInt(a -> a.start)
            );

            if (pos < 0) pos = -(pos + 1);

            // check left neighbor
            if (pos > 0 && overlaps(bookings.get(pos - 1), start, end)) {
                continue;
            }

            // check right neighbor
            if (pos < bookings.size() && overlaps(bookings.get(pos), start, end)) {
                continue;
            }

            // no overlap → schedule it
            bookings.add(pos, new Interval(start, end));
            String reservationId = room + "-" + UUID.randomUUID();
            return reservationId;
        }

        return "ERROR: No rooms available";
    }

    private boolean overlaps(Interval i, int s, int e) {
        return !(i.end <= s || e <= i.start);
    }
}
```

---

# 5. Time Complexity

Let:

* R = number of rooms (100 here)
* K = average number of meetings per room
* We use binary search + insertion in ArrayList.

### For each schedule request:

```
Worst-case: check all rooms
Room check: O(log K) binary search + O(K) insert
Overall: O(R * (log K + K))
```

For 100 rooms (small constant), this is fast.

### With balanced trees / interval trees:

You can reduce per room check to:

```
O(log K)
```

And overall:

```
O(R log K)
```

---

# 6. Scaling to Large N (Short Overview)

If you want to support:

* Thousands of rooms
* Millions of bookings

Use:

### Option 1: Min-Heap of rooms by next available time

Fast but only works for non-overlapping sequential bookings.

### Option 2: Interval Tree per room

Checking becomes O(log K).

### Option 3: Global segment tree / binary indexed tree

For large-scale, time-discretized scheduling.

### Option 4: Distributed sharding

Rooms partitioned across servers + consistent hashing.

---
