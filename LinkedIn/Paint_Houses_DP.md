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
int paint(int house, int color, int[][] costs, Map<Pair, Integer> memo) {
    if (memo.containsKey(new Pair(house, color))) return memo.get(...);
    int total = costs[house][color];
    if (house < costs.length - 1) {
        total += Math.min(
            paint(house + 1, (color + 1) % 3),
            paint(house + 1, (color + 2) % 3));
    }
    memo.put(...);
    return total;
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
int[] prev = costs[n - 1];
for (int i = n - 2; i >= 0; i--) {
    int[] curr = new int[3];
    curr[0] = costs[i][0] + Math.min(prev[1], prev[2]);
    curr[1] = costs[i][1] + Math.min(prev[0], prev[2]);
    curr[2] = costs[i][2] + Math.min(prev[0], prev[1]);
    prev = curr;
}
return Math.min(prev[0], Math.min(prev[1], prev[2]));
```

---

## DP Justification
- **Optimal Substructure**: Current decision depends on optimal decisions ahead.
- **Overlapping Subproblems**: Same sub-calls repeated in recursion.

This is a classic DP problem. Start with recursion for clarity, use memoization for optimization, then convert to iterative DP for cleaner and faster execution.
