# **Problem Summary**

You are given an `n × m` grid where each cell contains an arrow:

* `'R'` → points right
* `'L'` → points left
* `'U'` → points up
* `'D'` → points down

You start at `(0,0)` and must reach `(n−1, m−1)`.

### **Movement Cost**

* Moving *in the direction of the arrow* in that cell → **cost = 0**
* Moving *in any other direction* → **cost = 1**

You must find the **minimum cost** to reach the bottom-right cell.

---

# **Key Insight**

This is a shortest-path problem where edges have weight **0 or 1**.

The optimal solution uses **0–1 BFS**, which is faster than Dijkstra.

### Why 0–1 BFS?

* Works for graphs with only weights 0 and 1
* Uses a deque:

  * Push to **front** when cost = 0
  * Push to **back** when cost = 1
* Runs in **O(n*m)** time

---

# **Directions Mapping**

```
Right → (0, +1)
Left  → (0, -1)
Up    → (-1, 0)
Down  → (+1, 0)
```

---

# **Final Algorithm (0–1 BFS)**

1. Maintain a distance matrix initialized to infinity.
2. Use deque `(x, y)` starting from `(0, 0)`.
3. For each cell, try all 4 directions.
4. If moving matches the cell's arrow → cost = 0
   else → cost = 1.
5. If a better cost is found, push to deque:

   * cost 0 → push **front**
   * cost 1 → push **back**
6. Continue until reaching `(n−1, m−1)`.

---

# **Java Code (Optimal 0–1 BFS Solution)**

```java
import java.util.*;

class Solution {
    public int minCost(int[][] grid) {
        int n = grid.length, m = grid[0].length;

        int[][] dist = new int[n][m];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);

        Deque<int[]> dq = new ArrayDeque<>();
        dq.offer(new int[]{0, 0});
        dist[0][0] = 0;

        // Directions: R, L, D, U
        int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};

        while (!dq.isEmpty()) {
            int[] cur = dq.poll();
            int x = cur[0], y = cur[1];

            for (int d = 0; d < 4; d++) {
                int nx = x + dirs[d][0];
                int ny = y + dirs[d][1];

                if (nx < 0 || ny < 0 || nx >= n || ny >= m) continue;

                // Cost is 0 if direction matches arrow, else 1
                int cost = (grid[x][y] == d + 1) ? 0 : 1;

                if (dist[x][y] + cost < dist[nx][ny]) {
                    dist[nx][ny] = dist[x][y] + cost;

                    if (cost == 0)
                        dq.addFirst(new int[]{nx, ny});
                    else
                        dq.addLast(new int[]{nx, ny});
                }
            }
        }

        return dist[n-1][m-1];
    }
}
```

---

# **Time & Space Complexity**

### **Time: O(n*m)**

Each cell may be pushed into the deque only a few times.

### **Space: O(n*m)**

For the distance matrix and deque storage.

---
