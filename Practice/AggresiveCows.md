# **Question: Aggressive Cows (Binary Search on Answer)**

Farmer John has **N stalls** located on a straight line at positions
`x1, x2, ..., xN` (0 ≤ xi ≤ 1e9).

He wants to place **C cows** (C ≤ N) in these stalls such that:

### **The minimum distance between any two cows is as large as possible.**

Return this **largest minimum distance**.

**Input**

```
t test cases
For each test case:
N C
x1
x2
...
xN
```

**Output**
For each test case print one integer: the largest minimum distance.

**Example**

```
Input:
1
5 3
1
2
8
4
9

Output:
3
```

Explanation: Place cows at **1, 4, 8**, minimum distance = **3**.

---

# **Approach: Binary Search on Answer + Greedy Feasibility Check**

### ** Sort the stall positions**

Distances make sense only when stalls are ordered.

### ** Binary search for the answer D**

`D` = minimum distance we want to check.

Search range:

```
low = 1
high = stalls[n-1] - stalls[0]   // maximum possible distance
```

Why this?

* Minimum feasible distance can’t be < 1
* Maximum feasible distance can't exceed (max stall − min stall)

### ** Feasibility check (Greedy)**

Given a distance `D`, check if we can place `C` cows such that:

```
distance between adjacent cows ≥ D
```

Greedy logic:

* Place first cow at stalls[0]
* For each next stall, place a cow if
  `current_stall - last_placed_stall ≥ D`

If we place at least C cows → D is feasible.

### ** Binary search update rules**

* If D is feasible → try to increase distance
  `low = mid + 1`
* Else → reduce distance
  `high = mid - 1`

### ** Track best feasible D**

Answer = last feasible mid.

---

# **Java Optimized Solution**

```java
import java.util.*;

public class Main {

    // Greedy feasibility check
    private static boolean canPlace(int[] stalls, int cows, int dist) {
        int placed = 1; // first cow placed
        int lastPos = stalls[0];

        for (int i = 1; i < stalls.length; i++) {
            if (stalls[i] - lastPos >= dist) {
                placed++;
                lastPos = stalls[i];
                if (placed == cows) return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();
            int c = sc.nextInt();

            int[] stalls = new int[n];
            for (int i = 0; i < n; i++) stalls[i] = sc.nextInt();
            Arrays.sort(stalls);

            int low = 1;
            int high = stalls[n - 1] - stalls[0];
            int ans = 0;

            while (low <= high) {
                int mid = low + (high - low) / 2;

                if (canPlace(stalls, c, mid)) {
                    ans = mid;
                    low = mid + 1;  // try larger distance
                } else {
                    high = mid - 1; // try smaller distance
                }
            }

            System.out.println(ans);
        }

        sc.close();
    }
}
```

---

#  **Time Complexity**

### For each test case:

| Step                   | Complexity            |
| ---------------------- | --------------------- |
| Sorting stalls         | **O(N log N)**        |
| Binary search range    | log(maxDist) ≈ **30** |
| Each feasibility check | **O(N)**              |

Total:

### **O(N log N + N log(maxDistance)) ≈ O(N log N)**

Space Complexity: **O(1)** (ignoring input array)
