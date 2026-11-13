# **Expanding Tennis Club — Complete Interview Solution**

This problem is fundamentally an **Interval Partitioning / Meeting Rooms** problem.

The interviewer progressively adds constraints:

- Assign bookings to courts.
- Add turnaround time between bookings.
- Add maintenance after a fixed number of bookings.
- Calculate only the minimum number of courts.
- Detect conflicts between two bookings.

The key algorithm is **Greedy + Min Heap (PriorityQueue)**.

I'll use Java and explain how to evolve the solution for each part.

## **1. Part A — Assign bookings to minimum number of courts**

### **Problem understanding**

Each booking has:

- `id`
- `startTime`
- `finishTime`

We need to:

1. Assign every booking to a court.
2. Never allow overlapping bookings on the same court.
3. Use the minimum possible number of courts.

Example:

```
Booking    Start    Finish
B1           1        4
B2           2        5
B3           4        7
B4           5        8
B5           8       10
```

Visual representation:

Booking timeline

**Court 1**

**B1**

1–4

**B4**

5–8

**B5**

8–10

**Court 2**

**B2**

2–5

**B3**

5–7

Illustrative assignment. A booking ending at the exact start of another can reuse the court.

The minimum number of courts is 2.

### **Approach: Greedy + Min Heap**

The key observation:

**Always assign the next booking to the court that becomes available earliest.**

Why?

If the earliest available court cannot accommodate the booking, no other court can accommodate it either.

### **Algorithm**

1. Sort bookings by `startTime`.
2. Maintain a min heap of courts.
3. Each heap entry stores:
   - Court ID.
   - Time when the court becomes available.
4. For every booking:
   - Check the earliest available court.
   - If its finish time is less than or equal to the booking's start time, reuse it.
   - Otherwise, create a new court.
   - Update the court's availability.
5. Return the assignments.

### **Java implementation**

```

class BookingRecord {
    int id;
    int startTime;
    int finishTime;

    BookingRecord(int id, int startTime, int finishTime) {
        this.id = id;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}

class Court {
    int courtId;
    int availableAt;
    List<BookingRecord> bookings = new ArrayList<>();

    Court(int courtId) {
        this.courtId = courtId;
    }
}

public List<Court> assignCourts(
        List<BookingRecord> bookings) {

    bookings.sort(
        Comparator.comparingInt(b -> b.startTime)
    );

    PriorityQueue<Court> minHeap =
        new PriorityQueue<>(
            Comparator.comparingInt(c -> c.availableAt)
        );

    List<Court> result = new ArrayList<>();

    for (BookingRecord booking : bookings) {

        Court court;

        if (!minHeap.isEmpty() &&
            minHeap.peek().availableAt <= booking.startTime) {

            court = minHeap.poll();

        } else {

            court = new Court(result.size() + 1);
            result.add(court);
        }

        court.bookings.add(booking);
        court.availableAt = booking.finishTime;

        minHeap.offer(court);
    }

    return result;
}

```

### **Complexity**

Let N be the number of bookings and C the number of courts created.

| Operation            | Complexity |
| -------------------- | ---------- |
| Sort bookings        | O(N log N) |
| Process each booking | O(log C)   |
| Total                | O(N log N) |
| Space                | O(N + C)   |

The number of courts is optimal.

## **2. Part B — Fixed maintenance time after every booking**

Now introduce a fixed maintenance period `X`.

After a booking finishes, the court requires X minutes before it can be rented again.

For example:

```
Booking 1: 10:00 - 11:00
Maintenance: 30 minutes

Court available again: 11:30
```

A booking starting at 11:15 cannot use this court.

### **What changes in the algorithm?**

Almost nothing!

Previously:

```
court.availableAt = booking.finishTime;
```

Now:

```
court.availableAt = booking.finishTime + maintenanceTime;
```

The same Min Heap algorithm works.

### **Java implementation**

```

public List<Court> assignCourts(
        List<BookingRecord> bookings,
        int maintenanceTime) {

    bookings.sort(
        Comparator.comparingInt(b -> b.startTime)
    );

    PriorityQueue<Court> minHeap =
        new PriorityQueue<>(
            Comparator.comparingInt(c -> c.availableAt)
        );

    List<Court> result = new ArrayList<>();

    for (BookingRecord booking : bookings) {

        Court court;

        if (!minHeap.isEmpty() &&
            minHeap.peek().availableAt <= booking.startTime) {

            court = minHeap.poll();

        } else {

            court = new Court(result.size() + 1);
            result.add(court);
        }

        court.bookings.add(booking);

        // Add maintenance time.
        court.availableAt =
            booking.finishTime + maintenanceTime;

        minHeap.offer(court);
    }

    return result;
}

```

**Complexity remains O(N log N).**

The important interview observation: maintenance is simply additional unavailability time. We don't need a separate maintenance scheduling algorithm for this requirement.

## **3. Part C — Maintenance after every X bookings, lasting Y time**

Now the requirement becomes more interesting.

Each court:

- Can accommodate X bookings.
- After completing X bookings, it requires Y units of maintenance.
- After maintenance, its booking counter resets.

Example:

```
Durability (X) = 3 bookings
Maintenance (Y) = 60 minutes

Court 1:

Booking 1: 09:00 - 10:00
Booking 2: 10:00 - 11:00
Booking 3: 11:00 - 12:00

Maintenance: 12:00 - 13:00

Booking 4: Earliest possible start = 13:00
```

### **3.1 Modify the Court class**

We need one additional field: `bookingsSinceMaintenance`.

```

class Court {
    int courtId;
    int availableAt;
    int bookingsSinceMaintenance;

    List<BookingRecord> bookings = new ArrayList<>();

    Court(int courtId) {
        this.courtId = courtId;
        this.availableAt = 0;
        this.bookingsSinceMaintenance = 0;
    }
}

```

### **3.2 Algorithm**

The overall algorithm remains the same.

The difference is how we calculate the next availability time.

For each booking:

1. Select the earliest available court.
2. Assign the booking.
3. Increment its booking counter.
4. If the counter reaches durability:
   - Add maintenance time Y.
   - Reset the counter to zero.
5. Insert the court back into the heap.

### **3.3 Java implementation**

```

public List<Court> assignCourtWithMaintenance(
        List<BookingRecord> bookings,
        int maintenanceTime,
        int durability) {

    if (durability <= 0 || maintenanceTime < 0) {
        throw new IllegalArgumentException(
            "Invalid maintenance configuration"
        );
    }

    bookings.sort(
        Comparator.comparingInt(b -> b.startTime)
    );

    PriorityQueue<Court> minHeap =
        new PriorityQueue<>(
            Comparator.comparingInt(c -> c.availableAt)
        );

    List<Court> result = new ArrayList<>();

    for (BookingRecord booking : bookings) {

        Court court;

        if (!minHeap.isEmpty() &&
            minHeap.peek().availableAt <= booking.startTime) {

            court = minHeap.poll();

        } else {

            court = new Court(result.size() + 1);
            result.add(court);
        }

        // Assign booking.
        court.bookings.add(booking);
        court.bookingsSinceMaintenance++;

        // Calculate next availability.
        court.availableAt = booking.finishTime;

        // Maintenance after every X bookings.
        if (court.bookingsSinceMaintenance == durability) {

            court.availableAt += maintenanceTime;

            court.bookingsSinceMaintenance = 0;
        }

        minHeap.offer(court);
    }

    return result;
}

```

### **3.4 Complexity**

| Metric             | Complexity |
| ------------------ | ---------- |
| Sorting            | O(N log N) |
| Assigning bookings | O(N log C) |
| Total              | O(N log N) |
| Space              | O(N + C)   |

### **Important interview edge case**

Suppose:

```
Durability = 2
Maintenance = 30 minutes

Booking 1: 09:00 - 10:00
Booking 2: 10:00 - 11:00
Booking 3: 11:15 - 12:00
```

After Booking 2, maintenance runs from 11:00 to 11:30.

Therefore, Booking 3 cannot use that court.

A new court must be allocated if no other court is available.

**One important assumption:** Durability counts completed bookings, not total hours of usage. If the requirement means cumulative playing hours instead, we need to track accumulated duration rather than booking count.

Also, this Part C signature does not include Part B's fixed turnaround time. If both requirements apply, add both periods when calculating availability.

## **4. Part D — Find only the minimum number of courts**

Now the interviewer removes the requirement to assign bookings to specific courts.

We only need to return the minimum number of courts required.

This is a simpler version of the same problem.

### **Approach 1: Min Heap**

1. Sort bookings by start time.
2. Maintain a min heap of booking finish times.
3. Before processing each booking, remove all bookings that have finished.
4. Add the current booking's finish time.
5. Track the maximum heap size.

Why does this work?

The heap represents bookings currently occupying courts. The maximum number of simultaneously active bookings is the minimum number of courts required.

### **Java implementation**

```

public int minCourtsRequired(
        List<BookingRecord> bookings) {

    if (bookings == null || bookings.isEmpty()) {
        return 0;
    }

    bookings.sort(
        Comparator.comparingInt(b -> b.startTime)
    );

    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    int maxCourts = 0;

    for (BookingRecord booking : bookings) {

        // Free all courts whose bookings have finished.
        while (!minHeap.isEmpty() &&
               minHeap.peek() <= booking.startTime) {

            minHeap.poll();
        }

        // Allocate a court for current booking.
        minHeap.offer(booking.finishTime);

        maxCourts = Math.max(maxCourts, minHeap.size());
    }

    return maxCourts;
}

```

**Complexity: O(N log N) time, O(N) space.**

### **Approach 2: Sweep Line Algorithm**

There is another elegant solution.

Instead of tracking courts, track events:

- Booking starts: `+1`
- Booking finishes: `-1`

Sort all events by time and calculate the running sum.

The maximum running sum gives the minimum number of courts.

Example:

```
Bookings:
[1, 4]
[2, 5]
[4, 7]

Events:
Time 1: +1
Time 2: +1
Time 4: -1
Time 4: +1
Time 5: -1
Time 7: -1
```

At time 4, the first booking finishes and the third begins. We process the finish before the start because a court can be reused immediately.

Maximum simultaneous bookings = 2.

Interview tip

Know both approaches:

- Min Heap: Better when you want to understand or extend court allocation.
- Sweep Line: Elegant when you only need the minimum count.

## **5. Part E — Check whether two bookings conflict**

This is a classic **Interval Overlap** problem.

Given two bookings:

```
Booking A: [10, 14]
Booking B: [12, 16]
```

They conflict because they overlap between 12 and 14.

### **Key observation**

Two intervals overlap if:

\\[ A.start < B.finish \quad \land \quad B.start < A.finish \\]

### **Java implementation**

```

public boolean hasConflict(
        BookingRecord a,
        BookingRecord b) {

    return a.startTime < b.finishTime &&
           b.startTime < a.finishTime;
}

```

### **Examples**

| Booking A | Booking B | Conflict? |
| --------- | --------- | --------- |
| [1, 5]    | [3, 7]    | Yes       |
| [1, 5]    | [5, 8]    | No        |
| [1, 5]    | [6, 9]    | No        |
| [1, 8]    | [2, 4]    | Yes       |
| [1, 10]   | [3, 7]    | Yes       |

**Important:** This assumes half-open intervals `[start, finish)`, meaning a court can be reused immediately when the previous booking finishes.

If endpoints are inclusive, use `<=` instead of `<`.

## **6. Final interview cheat sheet**

**Which algorithm should I use?**

| Part | Problem                                   | Technique                                             |
| ---- | ----------------------------------------- | ----------------------------------------------------- |
| A    | Assign bookings to courts                 | Greedy + Min Heap                                     |
| B    | Add fixed maintenance after every booking | Greedy + Min Heap                                     |
| C    | Maintenance after every X bookings        | Min Heap + per-court counter (with scheduling caveat) |
| D    | Minimum courts only                       | Min Heap / Sweep Line                                 |
| E    | Check two bookings                        | Interval overlap formula                              |

### **Five things to remember**

1. **Sort by start time** whenever processing bookings chronologically.
2. **Min Heap** helps identify the earliest available court.
3. **Maximum concurrent intervals** equals the minimum number of courts.
4. **Maintenance** extends the court's unavailability period.
5. **Interval conflict:** `a.start < b.end && b.start < a.end`.

### **One advanced point for Part C**

There is an important distinction between Parts A/B and C.

In A/B, earliest-available-court greedy is provably optimal. In C, the per-court maintenance counter introduces scheduling state. Simply adding a counter to the heap implementation gives a valid assignment, but the basic greedy choice is not, by itself, a proof of globally minimum court usage.

For a Staff-level interview, explicitly discuss whether the interviewer expects a valid allocation or a provably minimum allocation under periodic maintenance. That distinction can change the algorithm.

**My suggested practice order:** Solve E first, then D, then A, then B. Finally tackle C. This builds the underlying interval reasoning before introducing the more complicated scheduling constraint.
