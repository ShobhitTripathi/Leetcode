
# Longest Increasing Path in a Matrix

## Problem Statement

Given an `m x n` integers matrix, return the length of the longest increasing path in matrix.

From each cell, you can move in four directions: up, down, left, or right. You **may not move diagonally** or move outside the boundary (i.e., wrap-around is not allowed).

---

## Approach #2: DFS + Memoization ✅

### ✅ Intuition

In a naive DFS solution, the same cell can be visited multiple times through different paths, leading to redundant work.

To avoid repeated calculations, we **memoize** the results of each cell's longest increasing path. This way, any subproblem (cell) is computed only once.

### ✅ Memoization

Memoization is a caching technique to save results of expensive function calls so that when the same inputs occur again, the result can be directly returned from the cache.

---

## 🔍 Algorithm

1. Define a `dfs(x, y)` function that finds the longest increasing path starting at `(x, y)`.
2. If the value for `(x, y)` is already computed, return it.
3. Explore all 4 directions from the current cell.
4. If the neighbor's value is greater, recursively call `dfs` on it.
5. Track the maximum length from all paths and store the result in a memo table.
6. Repeat for every cell in the matrix and return the overall maximum.

---

## ✅ Java Code

```java
class Solution {
    private static final int[][] DIRS = {{0,1},{1,0},{0,-1},{-1,0}};
    private int[][] cache;
    private int m, n;

    public int longestIncreasingPath(int[][] matrix) {
        m = matrix.length;
        n = matrix[0].length;
        cache = new int[m][n];

        int maxLen = 0;

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                maxLen = Math.max(maxLen, dfs(matrix, i, j));
            }
        }

        return maxLen;
    }

    private int dfs(int[][] matrix, int x, int y) {
        if (cache[x][y] != 0) return cache[x][y];

        int max = 1;

        for (int[] dir : DIRS) {
            int newX = x + dir[0], newY = y + dir[1];

            if (newX >= 0 && newX < m && newY >= 0 && newY < n &&
                matrix[newX][newY] > matrix[x][y]) {
                max = Math.max(max, 1 + dfs(matrix, newX, newY));
            }
        }

        cache[x][y] = max;
        return max;
    }
}
```

---

## 📊 Complexity Analysis

### ⏱️ Time Complexity: `O(m * n)`

- Each cell `(i, j)` is computed **only once** and its result is stored.
- Each DFS call explores up to 4 neighbors.
- So total DFS calls = `m * n`, and each call does constant work due to memoization.

### 🧠 Space Complexity: `O(m * n)`

- The `cache[][]` table stores results for each cell.
- Stack space for recursion is also `O(mn)` in the worst case if the path goes through all cells.

---

## 🔁 Example Input

```text
matrix = [
  [9, 9, 4],
  [6, 6, 8],
  [2, 1, 1]
]
```

**Output:** `4`  
**Explanation:** The longest increasing path is `[1, 2, 6, 9]`.

---
