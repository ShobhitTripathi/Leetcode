3161. Block Placement Queries

There exists an infinite number line, with its origin at 0 and extending towards the positive x-axis.

You are given a 2D array queries, which contains two types of queries:

For a query of type 1, queries[i] = [1, x]. Build an obstacle at distance x from the origin. 
It is guaranteed that there is no obstacle at distance x when the query is asked.
For a query of type 2, queries[i] = [2, x, sz].
Check if it is possible to place a block of size sz anywhere in the range [0, x] on the line, such that the block entirely lies in the range [0, x]. 
A block cannot be placed if it intersects with any obstacle, but it may touch it. Note that you do not actually place the block. Queries are separate.
Return a boolean array results, where results[i] is true if you can place the block specified in the ith query of type 2, and false otherwise.

 

Example 1:
```
Input: queries = [[1,2],[2,3,3],[2,3,1],[2,2,2]]

Output: [false,true,true]

Explanation:
For query 0, place an obstacle at x = 2. A block of size at most 2 can be placed before x = 3.
```

Example 2:
```
Input: queries = [[1,7],[2,7,6],[1,2],[2,7,5],[2,7,6]]

Output: [true,true,false]

Explanation:
Place an obstacle at x = 7 for query 0. A block of size at most 7 can be placed before x = 7.
Place an obstacle at x = 2 for query 2. Now, a block of size at most 5 can be placed before x = 7, and a block of size at most 2 before x = 2.
``` 

Constraints:
```
1 <= queries.length <= 15 * 104
2 <= queries[i].length <= 3
1 <= queries[i][0] <= 2
1 <= x, sz <= min(5 * 104, 3 * queries.length)
The input is generated such that for queries of type 1, no obstacle exists at distance x when the query is asked.
The input is generated such that there is at least one query of type 2.
```

Approach
```
⏱️ 1-Minute Recap
1️⃣ Keep obstacles sorted using a TreeSet
This lets us quickly find:
the previous obstacle
the next obstacle
the rightmost obstacle ≤ x

All in O(log n).

2️⃣ Track all free gaps between obstacles in a TreeMap
Each time we insert an obstacle at position p, find neighbors L and R:
Remove old gap (R - L)
Add new gaps (p - L) and (R - p)
TreeMap lets us get the maximum gap in O(log n).

3️⃣ For query [2, x, sz]
Check three possible places where a block of size sz might fit:
Gap before the largest obstacle ≤ x
Gap after it → (x - R)
Global maximum gap from TreeMap
If any gap ≥ sz, return true; else false.

4️⃣ Time Complexity
Every operation is O(log n) due to TreeSet + TreeMap.
Total: O(Q log Q) — easily fits constraints.

```

Solution
```java
import java.util.*;

class Solution {
    
    // Helper to add a gap to gapCount map
    private void addGap(TreeMap<Integer, Integer> gapCount, int gap) {
        if (gap <= 0) return;
        gapCount.put(gap, gapCount.getOrDefault(gap, 0) + 1);
    }

    // Helper to remove a gap from gapCount map
    private void removeGap(TreeMap<Integer, Integer> gapCount, int gap) {
        if (gap <= 0) return;
        int cnt = gapCount.get(gap);
        if (cnt == 1) gapCount.remove(gap);
        else gapCount.put(gap, cnt - 1);
    }

    public boolean[] getResults(int[][] queries) {
        int n = queries.length;
        boolean[] ans = new boolean[n];

        TreeSet<Integer> obstacles = new TreeSet<>();
        TreeMap<Integer, Integer> gapCount = new TreeMap<>();

        // We treat 0 as a fixed left boundary obstacle for convenience.
        obstacles.add(0);

        // Keep track of result index for type2 queries
        int idx = 0;

        for (int[] q : queries) {

            // ===========================
            // TYPE 1: Insert obstacle
            // ===========================
            if (q[0] == 1) {
                int x = q[1];

                Integer R = obstacles.higher(x);
                if (R == null) R = Integer.MAX_VALUE;   // Treat unbounded end as +∞

                Integer L = obstacles.lower(x);
                if (L == null) L = 0;

                // Remove old gap (R - L)
                if (R != Integer.MAX_VALUE) {
                    removeGap(gapCount, R - L);
                }

                // Add two new gaps: (x - L) and (R - x)
                addGap(gapCount, x - L);
                if (R != Integer.MAX_VALUE) addGap(gapCount, R - x);

                // Insert the new obstacle
                obstacles.add(x);
            }

            // ===========================
            // TYPE 2: Query placement
            // ===========================
            else {
                int x = q[1];
                int sz = q[2];

                // Find rightmost obstacle <= x
                Integer R = obstacles.floor(x);

                if (R == null) {
                    // No obstacle at all: whole interval free
                    ans[idx++] = (x >= sz);
                    continue;
                }

                // Find left obstacle
                Integer L = obstacles.lower(R);
                if (L == null) L = 0;

                boolean possible = false;

                // 1. Check gap just before R
                if (R - L >= sz) possible = true;

                // 2. Check suffix gap [R, x]
                if (x - R >= sz) possible = true;

                // 3. Check global maximum gap from gapCount
                if (!possible && !gapCount.isEmpty()) {
                    int maxGap = gapCount.lastKey();
                    if (maxGap >= sz) possible = true;
                }

                ans[idx++] = possible;
            }
        }

        // Filter only type-2 answers
        boolean[] finalResults = new boolean[idx];
        System.arraycopy(ans, 0, finalResults, 0, idx);
        return finalResults;
    }
}

```



Time Complexity
```
Operation	Time
Insert obstacle	O(log n)
Query placement	O(log n)
Maintain gap map	O(log n)
Total	O(Q log Q)
Space	O(Q)
```
