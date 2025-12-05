2508. Add Edges to Make Degrees of All Nodes Even

There is an undirected graph consisting of n nodes numbered from 1 to n. 
You are given the integer n and a 2D array edges where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi. 
The graph can be disconnected.
You can add at most two additional edges (possibly none) to this graph so that there are no repeated edges and no self-loops.
Return true if it is possible to make the degree of each node in the graph even, otherwise return false.
The degree of a node is the number of edges connected to it.

Example 1:
```
Input: n = 5, edges = [[1,2],[2,3],[3,4],[4,2],[1,4],[2,5]]
Output: true
Explanation: The above diagram shows a valid way of adding an edge.
Every node in the resulting graph is connected to an even number of edges.
```
Example 2:
```
Input: n = 4, edges = [[1,2],[3,4]]
Output: true
Explanation: The above diagram shows a valid way of adding two edges.
```

Example 3:
```
Input: n = 4, edges = [[1,2],[1,3],[1,4]]
Output: false
Explanation: It is not possible to obtain a valid graph with adding at most 2 edges.
``` 

Constraints:
```
3 <= n <= 105
2 <= edges.length <= 105
edges[i].length == 2
1 <= ai, bi <= n
ai != bi
There are no repeated edges.
```

Approach
```
Count odd-degree nodes.
Only if odd count is 0, 2, or 4 can you fix degrees with ≤2 new edges.
For 2 or 4 odds, check if you can add edges without duplicates.
Try all valid pairings and return true if any succeeds.

```
Solution
```java
import java.util.*;

class Solution {
    public boolean isPossible(int n, int[][] edges) {
        
        // Graph: node -> set of neighbors
        Map<Integer, Set<Integer>> graph = new HashMap<>();

        // Build graph using computeIfAbsent
        for (int[] e : edges) {
            int u = e[0], v = e[1];
            graph.computeIfAbsent(u, k -> new HashSet<>()).add(v);
            graph.computeIfAbsent(v, k -> new HashSet<>()).add(u);
        }

        // Step 1: collect odd-degree nodes
        List<Integer> odd = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            int deg = graph.getOrDefault(i, Collections.emptySet()).size();
            if (deg % 2 == 1) odd.add(i);
        }

        int k = odd.size();

        // Case 0: already all even
        if (k == 0) return true;

        // Case 2 odd nodes
        if (k == 2) {
            int a = odd.get(0), b = odd.get(1);

            // Option 1: connect a and b directly
            if (!exists(graph, a, b)) return true;

            // Option 2: connect both to same node x
            for (int x = 1; x <= n; x++) {
                if (x != a && x != b) {
                    if (!exists(graph, a, x) && !exists(graph, b, x)) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Case 4 odd nodes
        if (k == 4) {
            int a = odd.get(0), b = odd.get(1), c = odd.get(2), d = odd.get(3);

            // Try the three possible pairings
            if (canAddPair(graph, a, b, c, d)) return true;
            if (canAddPair(graph, a, c, b, d)) return true;
            if (canAddPair(graph, a, d, b, c)) return true;

            return false;
        }

        // Any odd count other than 0, 2, 4 → impossible
        return false;
    }

    // Check if edge exists
    private boolean exists(Map<Integer, Set<Integer>> graph, int u, int v) {
        return graph.getOrDefault(u, Collections.emptySet()).contains(v);
    }

    // Check if both pairs can be added (neither edge should exist)
    private boolean canAddPair(Map<Integer, Set<Integer>> graph, int u1, int v1, int u2, int v2) {
        return !exists(graph, u1, v1) && !exists(graph, u2, v2);
    }
}

```
