# Graph Valid Tree

Given n nodes labeled from 0 to n - 1 and a list of undirected edges (each edge is a pair of nodes), write a function to check whether these edges make up a valid tree.

Example 1:
Input:
n = 5
edges = [[0, 1], [0, 2], [0, 3], [1, 4]]

Output:
true

Example 2:
Input:
n = 5
edges = [[0, 1], [1, 2], [2, 3], [1, 3], [1, 4]]
Output:
false

Note:
You can assume that no duplicate edges will appear in edges. Since all edges are undirected, [0, 1] is the same as [1, 0] and thus will not appear together in edges.

Constraints:
1 <= n <= 100
0 <= edges.length <= n * (n - 1) / 2



```java
class Solution {
    Map<Integer, List<Integer>> graph;
    Set<Integer> visited;

    public boolean validTree(int n, int[][] edges) {
        graph = new HashMap<>();
        visited = new HashSet<>();

        // Build undirected graph
        for (int[] edge : edges) {
            graph.computeIfAbsent(edge[0], k -> new ArrayList<>()).add(edge[1]);
            graph.computeIfAbsent(edge[1], k -> new ArrayList<>()).add(edge[0]);
        }

        // Check for cycles
        if (!dfs(0, -1)) {
            return false;
        }

        // Check if all nodes are connected
        return visited.size() == n;
    }

    private boolean dfs(int node, int parent) {
        if (visited.contains(node)) {
            return false; // Found a cycle
        }
        visited.add(node);

        for (int nei : graph.getOrDefault(node, Collections.emptyList())) {
            if (nei == parent) continue; // Don't revisit the parent
            if (!dfs(nei, node)) return false; // Cycle detected
        }

        return true;
    }
}


```
