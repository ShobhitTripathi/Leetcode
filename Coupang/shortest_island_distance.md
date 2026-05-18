Problem Statement
```
Given a grid[][] containing 0s and 1s, where '0' represents water and '1' represents the land.
Given that an island is a group of land (1s) surrounded by water (0s) on all sides.

The task is to find the distance between the two closest islands such that:

Distance between two islands is the minimum number of '0' between two islands.
Only 4 - directional movement is allowed.
There are at least 2 islands present in the grid.

Input: grid =
{{1, 1, 0, 1, 1}, 
{1, 1, 0, 0, 0},  
{0, 0, 0, 0, 0},  
{0, 0, 1, 1, 1}}
Output: 1
Explanation: There are three islands present in the grid. 
Nearest pair of islands have only 1 zero (bolded in input grid) in between them.

Input: grid =
{{1, 0, 0, 0, 1},  
{1, 1, 0, 0, 0},  
{0, 0, 0, 0, 0},  
{0, 0, 1, 1, 1}}
Output: 2
Explanation: There are three islands present in the grid. 
Nearest pair of islands have 2 zeroes in between them (depicted by bold 0 in input grid). 
In this case there are multiple pair of islands having a distance of 2 between them. 
```

Approach
```
First, label every island with a unique island id using BFS.
For example, all cells of the first island get id `1`, second island gets id `2`, and so on.

Then, run **multi-source BFS** from all land cells together.
When BFS expansion from one island touches a cell already reached by another island,
we have found the minimum number of water cells between two islands.
```

Solution

```java
import java.util.*;

class Solution {
    // Global direction array for 4-directional movement
    static int[][] dirs = {
        {-1, 0}, // up
        {1, 0},  // down
        {0, -1}, // left
        {0, 1}   // right
    };

    static class Cell {
        int row, col, islandId, dist;

        Cell(int row, int col, int islandId, int dist) {
            this.row = row;
            this.col = col;
            this.islandId = islandId;
            this.dist = dist;
        }
    }

    static int shortestDistance(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        // id[i][j] stores which island owns this cell.
        // 0 means the cell is not visited yet.
        int[][] id = new int[n][m];

        // dist[i][j] stores distance from the island that reached this cell.
        int[][] dist = new int[n][m];
        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }

        Queue<Cell> q = new LinkedList<>();
        int islandId = 0;

        // Step 1: Label all islands with unique ids
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1 && id[i][j] == 0) {
                    islandId++;
                    markIsland(grid, id, dist, q, i, j, islandId);
                }
            }
        }

        // Step 2: Multi-source BFS from all island cells
        while (!q.isEmpty()) {
            Cell curr = q.poll();

            for (int[] dir : dirs) {
                int nr = curr.row + dir[0];
                int nc = curr.col + dir[1];

                if (nr < 0 || nr >= n || nc < 0 || nc >= m) {
                    continue;
                }

                // Unvisited water cell
                if (id[nr][nc] == 0) {
                    id[nr][nc] = curr.islandId;
                    dist[nr][nc] = curr.dist + 1;
                    q.offer(new Cell(nr, nc, curr.islandId, curr.dist + 1));
                }
                // Reached a cell already visited by another island
                else if (id[nr][nc] != curr.islandId) {
                    return dist[nr][nc] + curr.dist;
                }
            }
        }

        return -1;
    }

    static void markIsland(
        int[][] grid,
        int[][] id,
        int[][] dist,
        Queue<Cell> q,
        int sr,
        int sc,
        int islandId
    ) {
        int n = grid.length;
        int m = grid[0].length;

        Queue<int[]> bfs = new LinkedList<>();
        bfs.offer(new int[]{sr, sc});

        id[sr][sc] = islandId;
        dist[sr][sc] = 0;

        while (!bfs.isEmpty()) {
            int[] curr = bfs.poll();
            int r = curr[0];
            int c = curr[1];

            // Add every land cell as BFS source for step 2
            q.offer(new Cell(r, c, islandId, 0));

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr >= 0 && nr < n && nc >= 0 && nc < m &&
                    grid[nr][nc] == 1 && id[nr][nc] == 0) {

                    id[nr][nc] = islandId;
                    dist[nr][nc] = 0;
                    bfs.offer(new int[]{nr, nc});
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] grid1 = {
            {1, 1, 0, 1, 1},
            {1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1}
        };

        int[][] grid2 = {
            {1, 0, 0, 0, 1},
            {1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1}
        };

        System.out.println(shortestDistance(grid1)); // 1
        System.out.println(shortestDistance(grid2)); // 2
    }
}
```

Complexity
```
Time Complexity: `O(n * m)`
Each cell is visited a constant number of times.

Space Complexity: `O(n * m)`
Used by `id[][]`, `dist[][]`, and BFS queues.

```
