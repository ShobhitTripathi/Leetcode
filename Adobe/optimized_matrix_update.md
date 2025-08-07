
# Efficient Matrix Increment Using 2D Prefix Sum (Difference Array)

## Problem Statement

You are given:
- A positive integer `n`, indicating an `n x n` matrix filled initially with `0`s.
- A list of `queries`, where each query `[row1, col1, row2, col2]` adds `1` to all elements in the submatrix from `(row1, col1)` to `(row2, col2)` (inclusive).

You need to return the final matrix after processing all queries.

---

## Constraints

- `1 <= n <= 500`
- `1 <= queries.length <= 10^4`
- `0 <= row1 <= row2 < n`
- `0 <= col1 <= col2 < n`

---

## Optimized Approach: 2D Difference Array

### Why Optimization is Needed?
Brute-force processing each query by iterating over its submatrix is too slow: `O(q * n^2)` in the worst case. We need something faster.

### Idea

Use the **2D difference array technique**, where updates are applied lazily using boundaries. Later, a prefix sum reconstructs the final values.

### Steps

1. **Initialize a matrix `diff` of size (n+1)x(n+1)** to support boundary updates.
2. **For each query `[r1, c1, r2, c2]`**, update the corners in the `diff` matrix.
3. **Reconstruct the final matrix using 2D prefix sum**.

---

## Code (Java)

```java
class Solution {
    public int[][] rangeAddQueries(int n, int[][] queries) {
        int[][] diff = new int[n + 1][n + 1];

        for (int[] q : queries) {
            int r1 = q[0], c1 = q[1], r2 = q[2], c2 = q[3];

            diff[r1][c1] += 1;
            if (c2 + 1 < n) diff[r1][c2 + 1] -= 1;
            if (r2 + 1 < n) diff[r2 + 1][c1] -= 1;
            if (r2 + 1 < n && c2 + 1 < n) diff[r2 + 1][c2 + 1] += 1;
        }

        // First compute row-wise prefix sum
        for (int i = 0; i < n; ++i) {
            for (int j = 1; j < n; ++j) {
                diff[i][j] += diff[i][j - 1];
            }
        }

        // Then compute column-wise prefix sum
        for (int j = 0; j < n; ++j) {
            for (int i = 1; i < n; ++i) {
                diff[i][j] += diff[i - 1][j];
            }
        }

        // Build final matrix
        int[][] result = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                result[i][j] = diff[i][j];
            }
        }
        return result;
    }
}
```

---

## Time and Space Complexity

- **Time Complexity**: `O(n^2 + q)` — Constant time updates and two prefix sum passes.
- **Space Complexity**: `O(n^2)` for the `diff` matrix.

---

## Example

### Input
```
n = 3
queries = [[1,1,2,2],[0,0,1,1]]
```

### Output
```
[[1,1,0],
 [1,2,1],
 [0,1,1]]
```

---

## Summary

The 2D difference array is a powerful tool to apply multiple range updates efficiently. This approach ensures each query is handled in constant time and final matrix is built in linear time.
