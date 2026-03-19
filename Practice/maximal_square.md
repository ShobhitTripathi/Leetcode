# 221. Maximal Square

Given an m x n binary matrix filled with 0's and 1's, find the largest square containing only 1's and return its area.

Example 1:
```
Input: matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
Output: 4
```
Example 2:
```
Input: matrix = [["0","1"],["1","0"]]
Output: 1
```
Example 3:
```
Input: matrix = [["0"]]
Output: 0
```
Constraints:
```
m == matrix.length
n == matrix[i].length
1 <= m, n <= 300
matrix[i][j] is '0' or '1'.
```

# Solution

```java
class Solution {
    private int ROWS, COLS, res = 0;;
    private int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public int maximalSquare(char[][] matrix) {
        ROWS = matrix.length;
        COLS = matrix[0].length;

        for (int r = 0;r < ROWS;r++) {
            for (int c = 0; c < COLS;c++) {
                if (matrix[r][c] == '1') {
                    System.out.println(dfs(r, c, matrix));
                }
            }
        }
        return 0; 
    }

    private int dfs(int r, int c, char[][] matrix){
        if (r >= ROWS || c >= COLS || r < 0 || c < 0 || matrix[r][c] == '0') {
            return 0;
        }

        res += 1;

        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (matrix[nr][nc] == '1') {
                dfs(nr, nc, matrix);
            }
        }
        return res;
    }
 }

```
