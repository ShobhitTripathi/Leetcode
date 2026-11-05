Problem Statement
```
2577. Minimum Time to Visit a Cell In a Grid

You are given a m x n matrix grid consisting of non-negative integers where grid[row][col] represents the minimum time required to be able to visit the cell (row, col), 
which means you can visit the cell (row, col) only when the time you visit it is greater than or equal to grid[row][col].

You are standing in the top-left cell of the matrix in the 0th second, and you must move to any adjacent cell in the four directions: up, down, left, and right. 
Each move you make takes 1 second.

Return the minimum time required in which you can visit the bottom-right cell of the matrix. 
If you cannot visit the bottom-right cell, then return -1.

 
Example 1:

Input: grid = [[0,1,3,2],[5,1,2,5],[4,3,8,6]]
Output: 7
Explanation: One of the paths that we can take is the following:
- at t = 0, we are on the cell (0,0).
- at t = 1, we move to the cell (0,1). It is possible because grid[0][1] <= 1.
- at t = 2, we move to the cell (1,1). It is possible because grid[1][1] <= 2.
- at t = 3, we move to the cell (1,2). It is possible because grid[1][2] <= 3.
- at t = 4, we move to the cell (1,1). It is possible because grid[1][1] <= 4.
- at t = 5, we move to the cell (1,2). It is possible because grid[1][2] <= 5.
- at t = 6, we move to the cell (1,3). It is possible because grid[1][3] <= 6.
- at t = 7, we move to the cell (2,3). It is possible because grid[2][3] <= 7.
The final time is 7. It can be shown that it is the minimum time possible.

Example 2:

Input: grid = [[0,2,4],[3,2,1],[1,0,4]]
Output: -1
Explanation: There is no path from the top left to the bottom-right cell.

```

Algorithm
```
Approach: Modified Dijkstra's Algorithm

Check if both initial moves (right and down) in the grid require more than 1 second:

If both grid[0][1] > 1 and grid[1][0] > 1, return -1 because it’s impossible to proceed.
Initialize variables:

rows and cols store the dimensions of the grid.
directions array defines the possible moves: down, up, right, and left.
visited array keeps track of visited cells.
pq is a priority queue that stores {time, row, col} tuples, ordered by minimum time to reach each cell.
Add the starting point (top-left cell) to the priority queue with its initial time (grid[0][0]).

While the priority queue is not empty:

Poll the cell with the minimum time (time, row, col).

If the target cell (bottom-right) is reached, return the time.

Skip the current cell if it has already been visited.

Mark the current cell as visited.

For each of the four possible directions:

Calculate the next cell coordinates (nextRow, nextCol).
If the cell is valid (within bounds and not visited), calculate the additional wait time for the next cell:
If the difference between the grid value and the current time is even, the additional wait time is 1.
Otherwise, the wait time is 0.
Calculate the next possible time based on the grid value and the wait time, and add the new {nextTime, nextRow, nextCol} to the priority queue.
If the loop ends without reaching the target, return -1 (no path found).

Helper function isValid:

Check if a cell is within bounds and has not been visited.

```

Solution
```java

class Solution {

    public int minimumTime(int[][] grid) {
        // If both initial moves require more than 1 second, impossible to proceed
        if (grid[0][1] > 1 && grid[1][0] > 1) {
            return -1;
        }

        int rows = grid.length, cols = grid[0].length;
        // Possible movements: down, up, right, left
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        boolean[][] visited = new boolean[rows][cols];

        // Priority queue stores {time, row, col}
        // Ordered by minimum time to reach each cell
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) ->
            Integer.compare(a[0], b[0])
        );
        pq.add(new int[] { grid[0][0], 0, 0 });

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int time = curr[0], row = curr[1], col = curr[2];

            // Check if reached target
            if (row == rows - 1 && col == cols - 1) {
                return time;
            }

            // Skip if cell already visited
            if (visited[row][col]) {
                continue;
            }
            visited[row][col] = true;

            // Try all four directions
            for (int[] d : directions) {
                int nextRow = row + d[0], nextCol = col + d[1];
                if (!isValid(visited, nextRow, nextCol)) {
                    continue;
                }

                // Calculate the wait time needed to move to next cell
                int waitTime = ((grid[nextRow][nextCol] - time) % 2 == 0)
                    ? 1
                    : 0;
                int nextTime = Math.max(
                    grid[nextRow][nextCol] + waitTime,
                    time + 1
                );
                pq.add(new int[] { nextTime, nextRow, nextCol });
            }
        }
        return -1;
    }

    // Checks if given cell coordinates are valid and unvisited
    private boolean isValid(boolean[][] visited, int row, int col) {
        return (
            row >= 0 &&
            col >= 0 &&
            row < visited.length &&
            col < visited[0].length &&
            !visited[row][col]
        );
    }
}

```
