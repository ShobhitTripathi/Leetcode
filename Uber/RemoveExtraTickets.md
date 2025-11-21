# **Problem Restatement**

You have a theater with **N seats** numbered **1 to N**.

Due to a bug, **every customer received 2 tickets instead of 1**.
Each ticket is a seat number.

You are given a list `bookings`, where **each element represents the two seats assigned to the i-th customer**.

Example:

```
Customer 1 → (6, 2)
Customer 2 → (1, 2)
Customer 3 → (1, 6)
Customer 4 → (2, 3)
Customer 5 → (3, 4)
Customer 6 → (5, 2)
```

**Goal**
You want to cancel *exactly one ticket per person*.

You must check:

1. Is it possible to cancel 1 ticket for each customer such that:

   * The remaining chosen seat for each customer is **unique**
   * All remaining seats belong to the range **1..N**

2. If yes → return an array of size M (number of customers), where `result[i]` is the **cancelled seat** for customer i.

---

# **Interpretation**

Each customer has **two seat options**.
We must choose **one to keep**, and **one to cancel**, such that:

* All *kept* seats are distinct.
* All *kept* seats are within `1…N`.

This is a **graph bipartite matching** problem:

* Left side: customers
* Right side: seats
* Each customer is connected to two seats (their two tickets)

We need to find a **perfect matching**:
Each customer matched to exactly **one** allowable seat.

If a perfect matching exists → Yes
Otherwise → No

---

# **Given Example**

```
N = 5
Bookings:
(6,2)
(1,2)
(1,6)
(2,3)
(3,4)
(5,2)
```

Let’s find a unique valid seat for each:

We pick one seat to *keep*, one to *cancel*.

### Matchings Found

Customer 1 → keep 6, cancel 2
Customer 2 → keep 1, cancel 2
Customer 3 → keep 2, cancel 6
Customer 4 → keep 3, cancel 2
Customer 5 → keep 4, cancel 3
Customer 6 → keep 5, cancel 2

Thus cancel list:

```
(1,2,2,3,2)
```

(Same as your answer)

So the final is **Yes**, we can fix it by cancelling these.

---

# **Approach (Minimal Explanation)**

1. Treat each (ticket1, ticket2) as edges from customer to two possible seats.
2. Use **maximum bipartite matching** (Hungarian / DFS-based matching).
3. If we can match all customers → cancel the *other* seat for each customer.
4. Else → return "No".

This is the classic **2-choice assignment problem**.

---

# **Best Solution (Java, DFS-based Bipartite Matching)**

```java
import java.util.*;

class Solution {
    public int[] fixBookings(int N, int[][] bookings) {
        int M = bookings.length;

        // Seat assignment: seat → which customer is taking it
        int[] seatAssignedTo = new int[N + 1];
        Arrays.fill(seatAssignedTo, -1);

        // Try to assign each customer
        for (int cust = 0; cust < M; cust++) {
            boolean[] visited = new boolean[N + 1];
            if (!tryAssign(cust, bookings, seatAssignedTo, visited)) {
                return null;  // Not possible
            }
        }

        // Build cancel list
        int[] cancel = new int[M];
        for (int cust = 0; cust < M; cust++) {
            int keep = seatAssignedToInverse(seatAssignedTo, cust);
            int a = bookings[cust][0], b = bookings[cust][1];
            cancel[cust] = (keep == a ? b : a);
        }

        return cancel;
    }

    private boolean tryAssign(int cust, int[][] bookings, int[] assigned, boolean[] visited) {
        for (int seat : bookings[cust]) {
            if (seat < 1 || seat >= assigned.length) continue;

            if (visited[seat]) continue;
            visited[seat] = true;

            // If seat is free OR we can reassign the previous customer
            if (assigned[seat] == -1 ||
                tryAssign(assigned[seat], bookings, assigned, visited)) {
                assigned[seat] = cust;
                return true;
            }
        }
        return false;
    }

    private int seatAssignedToInverse(int[] seatAssignedTo, int cust) {
        for (int seat = 1; seat < seatAssignedTo.length; seat++) {
            if (seatAssignedTo[seat] == cust) return seat;
        }
        throw new RuntimeException("Customer not assigned!");
    }
}
```

---

# **Time Complexity**

* Each customer tries two seats → DFS
* Worst-case bipartite matching complexity:

```
O(M * N)
```

Where:

* M = number of customers
* N = number of seats

Efficient and fast for typical constraints.

---
