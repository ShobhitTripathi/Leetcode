# 864. Shortest Path to Get All Keys

## Problem Statement

You are given an `m x n` grid where:

- `'.'` is an empty cell.  
- `'#'` is a wall.  
- `'@'` is the starting point.  
- Lowercase letters represent keys.  
- Uppercase letters represent locks.  

You start at the starting point and one move consists of walking one space in one of the four cardinal directions. You cannot walk outside the grid or into a wall.

If you walk over a key, you pick it up. You cannot walk over a lock unless you have its corresponding key.

The goal is to **acquire all the keys**, not necessarily to open all locks.

### Constraints

- `1 <= m, n <= 30`  
- Grid contains only valid characters (`@`, `.`, `#`, a-z, A-Z)  
- Exactly one `@` in the grid  
- The number of keys is in the range `[1, 6]`  
- Each key is unique and has a corresponding lock  

---

## Approach 2: Points of Interest + Dijkstra

### Intuition

We only care about navigating between points of interest: the starting position, keys, and locks. A path can be broken into **primitive segments** between two such points, and we can precompute the cost of each segment using **BFS**.

This lets us model the problem as a **shortest-path graph** and solve it using **Dijkstra’s algorithm**. Each node represents:

- Our current location (point of interest)
- Our current key state (as a bitmask)

---

## Java Code

```java
import java.awt.Point;

class Solution {
    int INF = Integer.MAX_VALUE;
    String[] grid;
    int R, C;
    Map<Character, Point> location;
    int[] dr = new int[]{-1, 0, 1, 0};
    int[] dc = new int[]{0, -1, 0, 1};

    public int shortestPathAllKeys(String[] grid) {
        this.grid = grid;
        R = grid.length;
        C = grid[0].length();

        location = new HashMap();
        for (int r = 0; r < R; ++r)
            for (int c = 0; c < C; ++c) {
                char v = grid[r].charAt(c);
                if (v != '.' && v != '#')
                    location.put(v, new Point(r, c));
            }

        int targetState = (1 << (location.size() / 2)) - 1;
        Map<Character, Map<Character, Integer>> dists = new HashMap();
        for (char place: location.keySet())
            dists.put(place, bfsFrom(place));

        PriorityQueue<ANode> pq = new PriorityQueue<ANode>((a, b) ->
                Integer.compare(a.dist, b.dist));
        pq.offer(new ANode(new Node('@', 0), 0));
        Map<Node, Integer> finalDist = new HashMap();
        finalDist.put(new Node('@', 0), 0);

        while (!pq.isEmpty()) {
            ANode anode = pq.poll();
            Node node = anode.node;
            int d = anode.dist;
            if (finalDist.getOrDefault(node, INF) < d) continue;
            if (node.state == targetState) return d;

            for (char destination: dists.get(node.place).keySet()) {
                int d2 = dists.get(node.place).get(destination);
                int state2 = node.state;
                if (Character.isLowerCase(destination))
                    state2 |= (1 << (destination - 'a'));
                if (Character.isUpperCase(destination))
                    if ((node.state & (1 << (destination - 'A'))) == 0)
                        continue;

                if (d + d2 < finalDist.getOrDefault(new Node(destination, state2), INF)) {
                    finalDist.put(new Node(destination, state2), d + d2);
                    pq.offer(new ANode(new Node(destination, state2), d + d2));
                }
            }
        }

        return -1;
    }

    public Map<Character, Integer> bfsFrom(char source) {
        int sr = location.get(source).x;
        int sc = location.get(source).y;
        boolean[][] seen = new boolean[R][C];
        seen[sr][sc] = true;
        int curDepth = 0;
        Queue<Point> queue = new LinkedList();
        queue.offer(new Point(sr, sc));
        queue.offer(null);
        Map<Character, Integer> dist = new HashMap();

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (p == null) {
                curDepth++;
                if (!queue.isEmpty())
                    queue.offer(null);
                continue;
            }

            int r = p.x, c = p.y;
            if (grid[r].charAt(c) != source && grid[r].charAt(c) != '.') {
                dist.put(grid[r].charAt(c), curDepth);
                continue;
            }

            for (int i = 0; i < 4; ++i) {
                int cr = r + dr[i], cc = c + dc[i];
                if (0 <= cr && cr < R && 0 <= cc && cc < C && !seen[cr][cc]){
                    if (grid[cr].charAt(cc) != '#') {
                        queue.offer(new Point(cr, cc));
                        seen[cr][cc] = true;
                    }
                }
            }
        }

        return dist;
    }
}

class ANode {
    Node node;
    int dist;

    ANode(Node n, int d) {
        node = n;
        dist = d;
    }
}

class Node {
    char place;
    int state;

    Node(char p, int s) {
        place = p;
        state = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node other = (Node) o;
        return (place == other.place && state == other.state);
    }

    @Override
    public int hashCode() {
        return 256 * state + place;
    }
}

```
```
Time and Space Complexity
BFS Precomputation: O(P * R * C) where P is number of points of interest (≤13)
Dijkstra's Graph Search: O(P * 2^K * log(...))

Overall complexity is acceptable due to small number of keys (≤6)
```



```java
// Solution 2

import java.util.*;

class Solution {
    public int shortestPathAllKeys(String[] grid) {
        int m = grid.length, n = grid[0].length();
        int allKeys = 0, startRow = 0, startCol = 0;

        // 1. Find start and count total keys
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                char ch = grid[r].charAt(c);
                if (ch == '@') {
                    startRow = r;
                    startCol = c;
                } else if (ch >= 'a' && ch <= 'f') {
                    allKeys |= (1 << (ch - 'a'));  // Set bit for each key
                }
            }
        }

        // 2. Dijkstra/BFS: [row, col, keyMask, steps]
        Queue<int[]> queue = new LinkedList<>();
        boolean[][][] visited = new boolean[m][n][64]; // 2^6 = 64 possible key states

        queue.offer(new int[]{startRow, startCol, 0, 0});
        visited[startRow][startCol][0] = true;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0], c = curr[1], keys = curr[2], steps = curr[3];

            // If we've collected all keys
            if (keys == allKeys) return steps;

            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d], nc = c + dc[d];

                if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
                char ch = grid[nr].charAt(nc);
                int newKeys = keys;

                if (ch == '#') continue; // Wall

                // If it's a key, pick it up
                if (ch >= 'a' && ch <= 'f') {
                    newKeys |= (1 << (ch - 'a'));
                }

                // If it's a lock, check if we have the key
                if (ch >= 'A' && ch <= 'F' && (keys & (1 << (ch - 'A'))) == 0) {
                    continue;
                }

                if (!visited[nr][nc][newKeys]) {
                    visited[nr][nc][newKeys] = true;
                    queue.offer(new int[]{nr, nc, newKeys, steps + 1});
                }
            }
        }

        return -1;
    }
}
```

```
Key Points:
allKeys tracks what our final goal is (e.g., if there are 3 keys: 111 in binary → 7)
The queue stores: (row, col, current key bitmask, total steps)
We skip revisiting a state if (row, col, keyMask) has already been seen.
BFS naturally gives the shortest path since each move has equal cost.
``` 
