
Problem Statement
```
Given a 2D matrix where each cell contains one of four directions:

U -> Up
D -> Down
L -> Left
R -> Right


You are also given a source cell and a destination cell.

From any cell, you can move only in the direction written inside that cell.

Find whether it is possible to reach the destination from the source.

Example
grid = {
    {'R', 'R', 'D'},
    {'L', 'L', 'D'},
    {'U', 'R', 'L'}
};

source = (0, 0)
destination = (2, 2)

Output: true
```

Approach
```
This is a graph traversal problem.

Each cell is like a graph node.
From one cell, there is only one possible outgoing edge based on its direction.

We can simply do DFS or BFS from the source.

If we reach the destination, return `true`.

Use a `visited[][]` array to avoid infinite loops.
```

Solution

```java
import java.util.*;

class MatrixPath {

    // Global direction map
    static Map<Character, int[]> directionMap = new HashMap<>();

    static {
        directionMap.put('U', new int[]{-1, 0});
        directionMap.put('D', new int[]{1, 0});
        directionMap.put('L', new int[]{0, -1});
        directionMap.put('R', new int[]{0, 1});
    }

    static boolean isPossible(
        char[][] grid,
        int srcRow,
        int srcCol,
        int destRow,
        int destCol
    ) {
        int n = grid.length;
        int m = grid[0].length;

        // visited[i][j] tells whether this cell is already visited
        boolean[][] visited = new boolean[n][m];

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{srcRow, srcCol});
        visited[srcRow][srcCol] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();

            int row = curr[0];
            int col = curr[1];

            if (row == destRow && col == destCol) {
                return true;
            }

            char dir = grid[row][col];
            int[] move = directionMap.get(dir);

            int newRow = row + move[0];
            int newCol = col + move[1];

            // Move only if next cell is valid and not visited
            if (newRow >= 0 && newRow < n &&
                newCol >= 0 && newCol < m &&
                !visited[newRow][newCol]) {

                visited[newRow][newCol] = true;
                queue.offer(new int[]{newRow, newCol});
            }
        }

        return false;
    }

    public static void main(String[] args) {
        char[][] grid = {
            {'R', 'R', 'D'},
            {'L', 'L', 'D'},
            {'U', 'R', 'L'}
        };

        System.out.println(isPossible(grid, 0, 0, 2, 2)); // true
    }
}
```

Complexity
```
Time Complexity: O(N * M)
Space Complexity: O(N * M)
```

