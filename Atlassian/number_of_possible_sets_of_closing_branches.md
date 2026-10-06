Problem Statement
```
2959. Number of Possible Sets of Closing Branches

There is a company with n branches across the country, some of which are connected by roads. 
Initially, all branches are reachable from each other by traveling some roads.

The company has realized that they are spending an excessive amount of time traveling between their branches. 
As a result, they have decided to close down some of these branches (possibly none). 
However, they want to ensure that the remaining branches have a distance of at most maxDistance from each other.

The distance between two branches is the minimum total traveled length needed to reach one branch from another.

You are given integers n, maxDistance, and a 0-indexed 2D array roads, where roads[i] = [ui, vi, wi] represents 
the undirected road between branches ui and vi with length wi.

Return the number of possible sets of closing branches, so that any branch has a distance of at most maxDistance from any other.

Note that, after closing a branch, the company will no longer have access to any roads connected to it.

Note that, multiple roads are allowed.
```
 
Example 1:
```
Input: n = 3, maxDistance = 5, roads = [[0,1,2],[1,2,10],[0,2,10]]
Output: 5
Explanation: The possible sets of closing branches are:
- The set [2], after closing, active branches are [0,1] and they are reachable to each other within distance 2.
- The set [0,1], after closing, the active branch is [2].
- The set [1,2], after closing, the active branch is [0].
- The set [0,2], after closing, the active branch is [1].
- The set [0,1,2], after closing, there are no active branches.
It can be proven, that there are only 5 possible sets of closing branches.
```

Example 2:
```
Input: n = 3, maxDistance = 5, roads = [[0,1,20],[0,1,10],[1,2,2],[0,2,2]]
Output: 7
Explanation: The possible sets of closing branches are:
- The set [], after closing, active branches are [0,1,2] and they are reachable to each other within distance 4.
- The set [0], after closing, active branches are [1,2] and they are reachable to each other within distance 2.
- The set [1], after closing, active branches are [0,2] and they are reachable to each other within distance 2.
- The set [0,1], after closing, the active branch is [2].
- The set [1,2], after closing, the active branch is [0].
- The set [0,2], after closing, the active branch is [1].
- The set [0,1,2], after closing, there are no active branches.
It can be proven, that there are only 7 possible sets of closing branches.
```

Example 3:
```
Input: n = 1, maxDistance = 10, roads = []
Output: 2
Explanation: The possible sets of closing branches are:
- The set [], after closing, the active branch is [0].
- The set [0], after closing, there are no active branches.
It can be proven, that there are only 2 possible sets of closing branches.
```
 

Constraints:
```
1 <= n <= 10
1 <= maxDistance <= 105
0 <= roads.length <= 1000
roads[i].length == 3
0 <= ui, vi <= n - 1
ui != vi
1 <= wi <= 1000
```
All branches are reachable from each other by traveling some roads.


Approach

1. **Build an adjacency matrix** `graph[i][j]` containing the shortest direct road between branches `i` and `j`.

2. **Enumerate all possible sets of open branches** using a bitmask.  
   There are `2^n` possibilities.

3. For each subset:
   - Copy the graph into `dist`.
   - Run **Floyd-Warshall**, but use only **open branches** as intermediate nodes.
   - Check every pair of open branches.

4. If every pair has:
   ```text
   distance <= maxDistance
   ```
   then this subset is valid, so increment the answer.

5. **Complexity:**
   - Time: `O(2^n × n³)`
   - Space: `O(n²)`
  

Solution

```java

class Solution {

    public int numberOfSets(int n, int maxDistance, int[][] roads) {

        // A very large value representing "no path".
        // Don't use Integer.MAX_VALUE because adding two distances
        // can cause integer overflow.
        int INF = 1_000_000_000;

        /*
         * graph[i][j] = direct road distance from i to j.
         *
         * Initially:
         *      0   INF INF
         *      INF 0   INF
         *      INF INF 0
         */
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);

            // Distance from a node to itself is 0.
            graph[i][i] = 0;
        }

        /*
         * Build the graph.
         *
         * Multiple roads can exist between the same two branches,
         * so keep only the shortest direct road.
         */
        for (int[] road : roads) {

            int u = road[0];
            int v = road[1];
            int w = road[2];

            graph[u][v] = Math.min(graph[u][v], w);
            graph[v][u] = Math.min(graph[v][u], w);
        }

        int answer = 0;

        /*
         * Try every possible set of OPEN branches.
         *
         * Example for n = 3:
         *
         * 000 -> no branches open
         * 001 -> branch 0 open
         * 010 -> branch 1 open
         * ...
         * 111 -> all branches open
         *
         * There are 2^n possibilities.
         */
        for (int mask = 0; mask < (1 << n); mask++) {

            /*
             * We need a fresh distance matrix for every subset.
             *
             * Important:
             * We cannot calculate shortest paths once globally,
             * because a shortest path may go through a branch
             * that is CLOSED in the current subset.
             */
            int[][] dist = new int[n][n];

            for (int i = 0; i < n; i++) {
                dist[i] = graph[i].clone();
            }

            /*
             * Floyd-Warshall
             *
             * Try to improve the distance i -> j by going through k.
             *
             * i -----> k -----> j
             *
             * dist[i][j] =
             *     min(dist[i][j],
             *         dist[i][k] + dist[k][j])
             */
            for (int k = 0; k < n; k++) {

                // If k is CLOSED, we cannot use it as an
                // intermediate branch.
                if ((mask & (1 << k)) == 0) {
                    continue;
                }

                for (int i = 0; i < n; i++) {

                    // Ignore closed starting branches.
                    if ((mask & (1 << i)) == 0) {
                        continue;
                    }

                    for (int j = 0; j < n; j++) {

                        // Ignore closed destination branches.
                        if ((mask & (1 << j)) == 0) {
                            continue;
                        }

                        // Try going from i -> k -> j.
                        dist[i][j] = Math.min(
                            dist[i][j],
                            dist[i][k] + dist[k][j]
                        );
                    }
                }
            }

            /*
             * Now check whether every pair of OPEN branches
             * is within maxDistance.
             */
            boolean valid = true;

            for (int i = 0; i < n && valid; i++) {

                // Ignore closed branches.
                if ((mask & (1 << i)) == 0) {
                    continue;
                }

                for (int j = i + 1; j < n; j++) {

                    // Ignore closed branches.
                    if ((mask & (1 << j)) == 0) {
                        continue;
                    }

                    /*
                     * If even one pair is farther than maxDistance,
                     * this subset is invalid.
                     */
                    if (dist[i][j] > maxDistance) {
                        valid = false;
                        break;
                    }
                }
            }

            // This is a valid set of open branches.
            if (valid) {
                answer++;
            }
        }

        return answer;
    }
}

```
