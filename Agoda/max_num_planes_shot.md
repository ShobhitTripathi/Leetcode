# Maximum Number of Planes That Can Be Shot

---

## Problem Understanding

We are given:

* `A[i]` → Initial height of the `i-th` plane.
* `B[i]` → Speed at which the plane descends every second.
* We can shoot **only one plane per second**.
* Once a plane reaches the ground, it cannot be shot.

We need to find the **maximum number of planes** that can be destroyed.

---

# Step 1: Understand the Real Constraint

At first glance, the heights and speeds seem important.

But ask yourself:

> **What actually matters?**

Not the height itself.

Not the speed itself.

The only thing that matters is:

> **By what time must this plane be shot?**

---

## Finding the Deadline

Suppose

```
Height = 10
Speed  = 3
```

Timeline:

```
Second 0 : 10
Second 1 : 7
Second 2 : 4
Second 3 : 1
Second 4 : -2 (Hits ground)
```

The plane must be shot **before second 4**.

This deadline is

```
ceil(10 / 3) = 4
```

Similarly,

```
Height = 8
Speed = 2

Landing time = ceil(8/2)=4
```

Hence every plane can be converted into **one integer: its deadline**.

Formula:

```java
deadline = ceil(A[i] / B[i])
```

Using integer arithmetic,

```java
deadline = (A[i] + B[i] - 1) / B[i];
```

After this transformation, the original problem becomes:

```
We have N jobs.

Each job has a deadline.

One job takes exactly one second.

Find the maximum jobs that can be completed before their deadlines.
```

This is a classic **Greedy Scheduling** problem.

---

# Example

```
A = [1,3,5,4,8]
B = [1,2,2,1,2]
```

Deadlines become

```
1
2
3
4
4
```

---

# Observation

Suppose deadlines are

```
4
2
1
3
```

Should we shoot the plane with deadline **4** first?

No.

If we do,

```
Time 0 -> Deadline 4

Time 1 -> Deadline 1
```

The second plane has already crashed.

Instead,

always shoot

```
smallest deadline first
```

This is exactly the **Earliest Deadline First (EDF)** greedy strategy.

---

# Solution 1 — Sorting (O(N log N))

## Approach

1. Compute every landing deadline.
2. Sort all deadlines.
3. Shoot planes from smallest deadline to largest.
4. If current shooting time is still before the deadline, shoot it.
5. Otherwise stop.

---

## Code

```java
import java.util.Arrays;

class Solution {

    static int maxPlanes(int[] A, int[] B) {

        int n = A.length;
        int[] deadline = new int[n];

        // Convert each plane into its landing deadline.
        for (int i = 0; i < n; i++) {
            deadline[i] = (A[i] + B[i] - 1) / B[i];
        }

        // Earliest deadline first.
        Arrays.sort(deadline);

        int shotTime = 0;

        for (int d : deadline) {

            // If plane lands after our current shooting time,
            // we can destroy it.
            if (d > shotTime) {
                shotTime++;
            } else {
                // Plane has already landed.
                break;
            }
        }

        return shotTime;
    }

    public static void main(String[] args) {

        int[] A = {1,3,5,4,8};
        int[] B = {1,2,2,1,2};

        System.out.println(maxPlanes(A, B));
    }
}
```

---

## Dry Run

Deadlines

```
1 2 3 4 4
```

Current shooting time = 0

| Deadline | Can Shoot? | Shot Time |
| -------- | ---------- | --------- |
| 1        | Yes        | 1         |
| 2        | Yes        | 2         |
| 3        | Yes        | 3         |
| 4        | Yes        | 4         |
| 4        | No         | Stop      |

Answer

```
4
```

---

## Complexity

### Time

Computing deadlines

```
O(N)
```

Sorting

```
O(N log N)
```

Traversal

```
O(N)
```

Overall

```
O(N log N)
```

### Space

Deadline array

```
O(N)
```

---

# Can we do better?

Sorting is taking

```
O(N log N)
```

Can we remove sorting?

---

# Important Observation

We have

```
N planes.
```

Maximum planes we can ever shoot

```
N
```

Suppose

```
Deadline = 1000
```

and

```
N = 5
```

Will we ever reach second 1000?

No.

Even if every shot succeeds,

```
0
1
2
3
4
```

Only five shots are possible.

Therefore

```
Deadline 6
Deadline 100
Deadline 1000
```

all behave exactly the same.

We can safely replace them by

```
N + 1
```

This is the key optimization.

Now deadlines belong to

```
1 ... N+1
```

which is a very small range.

Instead of sorting,

we simply count frequencies.

---

# Solution 2 — Frequency Array (O(N))

## Approach

1. Compute landing deadline.
2. Clamp deadline to `N+1`.
3. Store frequency of each deadline.
4. Traverse deadlines from 1 to N+1.
5. Shoot greedily.

---

## Code

```java
class Solution {

    static int maxPlanes(int[] A, int[] B) {

        int n = A.length;

        // Frequency of each deadline.
        int[] freq = new int[n + 2];

        // Compute and clamp deadlines.
        for (int i = 0; i < n; i++) {

            int deadline = (A[i] + B[i] - 1) / B[i];

            // Any deadline larger than N behaves like N+1.
            deadline = Math.min(deadline, n + 1);

            freq[deadline]++;
        }

        int shotTime = 0;

        // Process deadlines in increasing order.
        for (int deadline = 1; deadline <= n + 1; deadline++) {

            while (freq[deadline] > 0) {

                // Current plane has already landed.
                if (shotTime >= deadline) {
                    return shotTime;
                }

                // Shoot this plane.
                shotTime++;

                freq[deadline]--;
            }
        }

        return shotTime;
    }

    public static void main(String[] args) {

        int[] A = {1,3,5,4,8};
        int[] B = {1,2,2,1,2};

        System.out.println(maxPlanes(A, B));
    }
}
```

---

# Dry Run

Deadlines

```
1 2 3 4 4
```

Frequency

```
Deadline : 1 2 3 4 5

Count    : 1 1 1 2 0
```

Simulation

```
Shot Time = 0

Deadline 1

Shoot

Shot Time = 1

Deadline 2

Shoot

Shot Time = 2

Deadline 3

Shoot

Shot Time = 3

Deadline 4

Shoot

Shot Time = 4

Deadline 4

Cannot shoot

Answer = 4
```

---

# Complexity

Computing deadlines

```
O(N)
```

Building frequency array

```
O(N)
```

Traversing frequency array

```
O(N)
```

Overall

```
O(N)
```

Space

```
O(N)
```

---

# Interview Thought Process (How to Derive the Solution)

This is the reasoning process you should remember during interviews:

1. **Ignore the height and speed initially.** Ask yourself: *What determines whether a plane can be shot?* Answer: the latest time before it reaches the ground.

2. **Convert each plane into a deadline** using:

   ```
   deadline = ceil(height / speed)
   ```

3. **Recognize the transformed problem.** It is now:

   > Schedule the maximum number of unit-time jobs before their deadlines.

4. **Recall the greedy strategy:** Always process the earliest deadline first (Earliest Deadline First).

5. **Implement with sorting** for an `O(N log N)` solution.

6. **Optimize further:** Notice that you can never shoot more than `N` planes, so deadlines larger than `N` are effectively identical. Clamp them to `N + 1` and replace sorting with a frequency array to achieve **`O(N)`**.

---

# Key Takeaways

* Transform the problem into one involving **deadlines**, not heights.
* The greedy rule is **Earliest Deadline First**.
* The standard solution is **Sort + Greedy (`O(N log N)`)**.
* The optimized solution is **Frequency Array + Greedy (`O(N)`)** by exploiting the bounded deadline range after clamping. This optimization is a good differentiator in interviews because it shows you can identify and leverage hidden constraints.
