# Paint Houses (Dynamic Programming)

## Problem
You're given a list of `n` houses, each with 3 costs [Red, Green, Blue]. Paint each house such that no two adjacent houses have the same color and minimize total cost.

---

## Refined Interview-Ready Approaches

### Approach 1: Brute Force
- **Idea**: Try all permutations of colors with constraints.
- **Time**: O(2^n) or O(3^n)
- **Space**: O(n)
- **Why Not?**: Exponential and impractical.

---

### Approach 2: Recursive Tree
- **Idea**: Model as a tree where each node represents painting a house a certain color.
- **Function**: `paint(houseIndex, color)`
- **Transition**: Choose current color + min(costs of next house in valid colors)
- **Time**: O(2^n)
- **Space**: O(n) stack space
- **Why Not?**: Still exponential due to repeated subproblems.

---

### Approach 3: Memoization (Top-Down DP)
- **Idea**: Cache repeated subproblems using a `Map<(houseIndex, color), cost>`
- **Function**: Same as recursive tree but store results.
- **Time**: O(n), since 3 colors * n houses = 3n subproblems
- **Space**: O(n) stack + O(n) memo

#### Java-like Code Snippet:
```java
class Solution {

    private int[][] costs;
    private Map<String, Integer> memo;

    public int minCost(int[][] costs) {
        if (costs.length == 0) {
            return 0;
        }
        this.costs = costs;
        this.memo = new HashMap<>();
        return Math.min(
            paintCost(0, 0),
            Math.min(paintCost(0, 1), paintCost(0, 2))
        );
    }

    private int paintCost(int n, int color) {
        if (memo.containsKey(getKey(n, color))) {
            return memo.get(getKey(n, color));
        }
        int totalCost = costs[n][color];
        if (n == costs.length - 1) {} else if (color == 0) { // Red
            totalCost += Math.min(paintCost(n + 1, 1), paintCost(n + 1, 2));
        } else if (color == 1) { // Green
            totalCost += Math.min(paintCost(n + 1, 0), paintCost(n + 1, 2));
        } else { // Blue
            totalCost += Math.min(paintCost(n + 1, 0), paintCost(n + 1, 1));
        }
        memo.put(getKey(n, color), totalCost);

        return totalCost;
    }

    private String getKey(int n, int color) {
        return String.valueOf(n) + " " + String.valueOf(color);
    }
}
```

---

### Approach 4: Bottom-Up DP (In-place)
- **Idea**: Modify input to accumulate optimal cost backwards.
- **Transition**: `cost[i][color] += min(cost[i+1][otherColors])`
- **Start**: From second-last house and go up.
- **Final Answer**: `min(cost[0][0], cost[0][1], cost[0][2])`
- **Time**: O(n)
- **Space**: O(1) (in-place)

---

### Approach 5: Bottom-Up DP (Space Optimized)
- **Idea**: Keep only previous and current row (length 3 arrays).
- **Why?**: Only immediate previous row needed for decision.
- **Time**: O(n)
- **Space**: O(1)

#### Java-like Code Snippet:
```java

class Solution {

    public int minCost(int[][] costs) {
        if (costs.length == 0) return 0;

        int[] previousRow = costs[costs.length - 1];

        for (int n = costs.length - 2; n >= 0; n--) {
            int[] currentRow = costs[n];

            // Total cost of painting the nth house red.
            currentRow[0] += Math.min(previousRow[1], previousRow[2]);
            // Total cost of painting the nth house green.
            currentRow[1] += Math.min(previousRow[0], previousRow[2]);
            // Total cost of painting the nth house blue.
            currentRow[2] += Math.min(previousRow[0], previousRow[1]);
            previousRow = currentRow;
        }

        return Math.min(
            Math.min(previousRow[0], previousRow[1]),
            previousRow[2]
        );
    }
}
```

---

## DP Justification
- **Optimal Substructure**: Current decision depends on optimal decisions ahead.
- **Overlapping Subproblems**: Same sub-calls repeated in recursion.

This is a classic DP problem. Start with recursion for clarity, use memoization for optimization, then convert to iterative DP for cleaner and faster execution.
