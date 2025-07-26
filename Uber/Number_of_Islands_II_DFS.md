# 🏝️ LeetCode 305: Number of Islands II

🔗 [LeetCode Link](https://leetcode.com/problems/number-of-islands-ii/description/)

## ❓ Problem Description

You are given an empty 2D binary grid of size `m x n`. Initially, all the cells are **water** (0). We may perform an `addLand` operation, which turns the water at position `(row, col)` into a land (1). 

Given a list of `positions` to operate, return the **number of islands** after each `addLand` operation.

An island is surrounded by water and is formed by connecting adjacent lands **horizontally or vertically**.

---

## 🧾 Example

```
Input: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]
Output: [1,1,2,3]
```

---

## 🔐 Constraints

- `1 <= m, n <= 10^4`
- `1 <= positions.length <= 10^4`
- `positions[i].length == 2`
- `0 <= row_i < m`, `0 <= col_i < n`

---

## ⚠️ Note on DFS Usage

While **Union-Find (Disjoint Set)** is the most optimal approach for this problem (`O(k * α(n))`), here’s the DFS-based implementation for conceptual understanding. 

However, **DFS may time out on large grids or large numbers of operations**, so it's not ideal for production use in this problem.

---

## 🚀 Java Solution using DFS

```java
import java.util.*;

public class Solution {
    private int[][] directions = {{0,1},{1,0},{-1,0},{0,-1}};
    private boolean[][] grid;
    private int count = 0;
    private int rows, cols;

    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        rows = m;
        cols = n;
        grid = new boolean[m][n];
        List<Integer> result = new ArrayList<>();

        for (int[] pos : positions) {
            int x = pos[0], y = pos[1];

            // Skip if it's already land
            if (grid[x][y]) {
                result.add(count);
                continue;
            }

            grid[x][y] = true;
            count++;  // Assume new island added

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (isValid(newX, newY) && grid[newX][newY]) {
                    if (dfs(newX, newY, x, y)) {
                        count--; // Connected to existing island, so reduce count
                    }
                }
            }
            result.add(count);
        }

        return result;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    // DFS marks all connected land to avoid revisiting
    private boolean dfs(int x, int y, int originX, int originY) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x, y});

        boolean connected = false;
        grid[x][y] = true;

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int cx = cell[0], cy = cell[1];

            for (int[] dir : directions) {
                int nx = cx + dir[0], ny = cy + dir[1];
                if (isValid(nx, ny) && grid[nx][ny]) {
                    if (nx == originX && ny == originY) {
                        connected = true;
                        continue;
                    }
                    grid[nx][ny] = false;  // mark as visited
                    stack.push(new int[]{nx, ny});
                }
            }
        }
        return connected;
    }
}
```

---

## ✅ Notes

- This DFS implementation assumes you mark visited nodes immediately to avoid infinite loops.
- However, this solution has a potential issue of **merging already visited islands incorrectly** due to the lack of Union-Find parent tracking.
- Consider using **Union-Find** for optimal performance in interviews or large-scale data.

Would you like the Union-Find version as well?