# **Problem: Robot Detection With Exact Blocker Distances**

You are given:

### **1. A 2D grid (`location map`)** containing:

* `'O'` → a robot
* `'E'` → empty space
* `'X'` → a blocker

Example:

```
| O | E | E | E | X |
| E | O | X | X | X |
| E | E | E | E | E |
| X | E | O | E | E |
| X | E | X | E | X |
```

### **2. A query array of size 4**:

```
[left, top, bottom, right]
```

Each value represents the **exact number of steps** the robot must be able to move in that direction **before hitting a blocker or boundary**.

### Important Notes

* You move 1 step at a time.
* Blockers (`X`) stop movement.
* Boundaries **also count as blockers**.
* The distance count includes the step into the blocker/boundary.

### **Goal**

Return the list of coordinates `[row, col]` for all robots that match the exact distance requirements in **all four directions**.

---

# **Example**

### Input Grid

```
| O | E | E | E | X |
| E | O | X | X | X |
| E | E | E | E | E |
| X | E | O | E | E |
| X | E | X | E | X |
```

### Query

```
[2, 2, 4, 1]
```

### Output

```
[[1, 1]]
```

### Explanation

Robot at **(1,1)** can move:

* Left  = 2 steps
* Top   = 2 steps
* Bottom = 4 steps
* Right = 1 step

And the first obstacle encountered in each path matches exactly the query.

---

# **Approach**

1. Iterate through the grid and locate each robot (`O`).
2. For each robot, measure distances in four directions:

   * Stop when encountering:

     * a blocker `X`, or
     * the boundary of the grid
   * The count includes the step into the blocker/boundary.
3. Compare the distances with the query.
4. If all four match, add the robot’s coordinates to the result.
5. Return all matching robot positions.

This approach simulates movement exactly as described and ensures robots match the precise constraints.

---

# **Best Solution (Java)**

```java
import java.util.*;

class Solution {
    public List<int[]> findRobots(char[][] grid, int[] query) {
        int rows = grid.length;
        int cols = grid[0].length;

        List<int[]> result = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c] != 'O') continue;

                int left = countSteps(grid, r, c, 0, -1);
                int top = countSteps(grid, r, c, -1, 0);
                int bottom = countSteps(grid, r, c, 1, 0);
                int right = countSteps(grid, r, c, 0, 1);

                if (left == query[0] &&
                    top == query[1] &&
                    bottom == query[2] &&
                    right == query[3]) {

                    result.add(new int[]{r, c});
                }
            }
        }

        return result;
    }

    private int countSteps(char[][] grid, int r, int c, int dr, int dc) {
        int rows = grid.length, cols = grid[0].length;
        int steps = 0;

        int nr = r + dr, nc = c + dc;

        while (true) {
            steps++;

            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                return steps; // boundary as blocker
            }

            if (grid[nr][nc] == 'X') {
                return steps; // blocker found
            }

            nr += dr;
            nc += dc;
        }
    }
}
```

---

# **Time Complexity**

Let:

* `R` = number of rows
* `C` = number of columns
* There are at most `R*C` robots

Each robot checks 4 directions and may travel up to max(R, C) steps.

### **Total Time:**

```
O(R * C * (R + C))
```

### **Space Complexity:**

```
O(1) extra space
```

---
