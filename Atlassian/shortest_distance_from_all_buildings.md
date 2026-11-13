Problem Statement
```
317. Shortest Distance from All Buildings

You are given an m x n grid grid of values 0, 1, or 2, where:

each 0 marks an empty land that you can pass by freely,
each 1 marks a building that you cannot pass through, and
each 2 marks an obstacle that you cannot pass through.
You want to build a house on an empty land that reaches all buildings in the shortest total travel distance. You can only move up, down, left, and right.

Return the shortest travel distance for such a house. If it is not possible to build such a house according to the above rules, return -1.

The total travel distance is the sum of the distances between the houses of the friends and the meeting point.

 

Example 1:


Input: grid = [[1,0,2,0,1],[0,0,0,0,0],[0,0,1,0,0]]
Output: 7
Explanation: Given three buildings at (0,0), (0,4), (2,2), and an obstacle at (0,2).
The point (1,2) is an ideal empty land to build a house, as the total travel distance of 3+3+1=7 is minimal.
So return 7.
Example 2:

Input: grid = [[1,0]]
Output: 1
Example 3:

Input: grid = [[1]]
Output: -1
```

Approach
```
Approach 1: BFS from Empty Land to All Buildings
Intuition

Our goal is to find the empty land cell with the shortest total distance to all buildings, so we must first find the shortest total distance to all buildings from each empty land cell.
As previously mentioned, this can be accomplished using BFS. 
For each empty cell (cell value equals 0) in the grid, start a BFS and sum all the distances to buildings (cell value equals 1) from this cell. 
We will also keep track of the number of buildings we have reached from this source cell (empty cell).
If we cannot reach all the buildings from the current empty cell, then it is not a valid empty cell. 
Furthermore, we can be certain that any cell visited during this BFS also cannot reach all of the buildings. 
So we will mark all cells visited during this BFS as obstacles to ensure that we do not start another BFS from this region.

```

Algorithm
```

For each empty cell (grid[i][j] equals 0), start a BFS:
In the BFS, traverse all 4-directionally adjacent cells that are not blocked or visited and keep track of the distance from the start cell. 
Each iteration adds 1 to the distance.
Every time we reach a building, increment buildings reached counter housesReached by 1, 
and increase the total distance distanceSum by the current distance (i.e., the distance from the start cell to the building).

If housesReached equals totalHouses, then return the total distance.
Otherwise, the starting cell (and every cell visited during this BFS) cannot reach all of the buildings. 
So set every visited empty land cell equal to 2 so that we do not start a new BFS from that cell and return INT_MAX.
Each time a total distance is returned from a BFS call, update the minimum distance (minDistance).
If it is possible to reach all buildings from any empty land cell, then return the minimum distance found. Otherwise, return -1.
```

Solution
```java
class Solution {
    private int bfs(int[][] grid, int row, int col, int totalHouses) {
        // Next four directions.
        int dirs[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        int rows = grid.length;
        int cols = grid[0].length;
        int distanceSum = 0;
        int housesReached = 0;
        
        // Queue to do a bfs, starting from (row, col) cell.
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{ row, col });
        
        // Keep track of visited cells.
        boolean[][] vis = new boolean[rows][cols];
        vis[row][col] = true;

        int steps = 0;
        while (!q.isEmpty() && housesReached != totalHouses) {
            for (int i = q.size(); i > 0; --i) {
                int[] curr = q.poll();
                row = curr[0];
                col = curr[1];
                
                // If this cell is a building, then add the distance from source to this cell
                // and we go past from this cell.
                if (grid[row][col] == 1) {
                    distanceSum += steps;
                    housesReached++;
                    continue;
                }
                
                // This cell was empty cell, hence traverse the next cells which is not a blockage.
                for (int[] dir : dirs) {
                    int nextRow = row + dir[0]; 
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols) {
                        if (!vis[nextRow][nextCol] && grid[nextRow][nextCol] != 2) {
                            vis[nextRow][nextCol] = true;
                            q.offer(new int[]{ nextRow, nextCol });
                        }
                    }
                }
            }
            
            // After traversing one level of cells, increment the steps by 1 to reach to next level.
            steps++;
        }

        // If we did not reach all buildings, then any cell visited also cannot reach all buildings.
        // Set all cells visted to 2 so we do not check them again and return MAX_VALUE.
        if (housesReached != totalHouses) {
            for (row = 0; row < rows; row++) {
                for (col = 0; col < cols; col++) {
                    if (grid[row][col] == 0 && vis[row][col]) {
                        grid[row][col] = 2;
                    }
                }
            }
            return Integer.MAX_VALUE;
        }
        
        // If we have reached all buildings then return the total distance calculated.
        return distanceSum;
    }
    
    public int shortestDistance(int[][] grid) {
        int minDistance = Integer.MAX_VALUE;
        int rows = grid.length; 
        int cols = grid[0].length;
        int totalHouses = 0;
        
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (grid[row][col] == 1) {
                    totalHouses++;
                }
            }
        }
        
        // Find the min distance sum for each empty cell.
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (grid[row][col] == 0) {
                    minDistance = Math.min(minDistance, bfs(grid, row, col, totalHouses));
                }
            }
        }
        
        // If it is impossible to reach all buildings from any empty cell, then return -1.
        if (minDistance == Integer.MAX_VALUE) {
            return -1;
        }
        
        return minDistance;
    }
};
```
