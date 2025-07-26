# 🏝️ LeetCode 305: Number of Islands II — Optimized Union-Find Solution

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

## 💡 Optimal Approach: Union-Find (Disjoint Set)

- Use a **Union-Find** data structure to keep track of connected components (islands).
- Each position is mapped to a 1D index using: `index = row * n + col`.
- For each land addition:
  - Check 4-directional neighbors.
  - If neighbor is land, union the sets.
  - Keep track of number of distinct roots (islands).

Time Complexity: `O(k * α(n))` where `α` is the inverse Ackermann function (very small).

---

## 🚀 Java Solution using Union-Find

```java
import java.util.*;

public class Solution {
    int[] parent, rank;
    int count = 0;

    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        parent = new int[m * n];
        rank = new int[m * n];
        Arrays.fill(parent, -1);

        List<Integer> result = new ArrayList<>();
        int[][] dirs = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        for (int[] pos : positions) {
            int r = pos[0], c = pos[1];
            int index = r * n + c;

            if (parent[index] != -1) {
                result.add(count);
                continue;
            }

            parent[index] = index;  // mark as land
            count++;

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                int nIndex = nr * n + nc;
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && parent[nIndex] != -1) {
                    union(index, nIndex);
                }
            }
            result.add(count);
        }

        return result;
    }

    private int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    private void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        count--;
    }
}
```

---

## ✅ Notes

- This is the **most optimal solution** to the problem.
- Works efficiently for large grids and high numbers of operations.
- Always prefer **Union-Find** for dynamic connectivity problems like this one.

Let me know if you’d like the code in another language or want to walk through the logic step-by-step.