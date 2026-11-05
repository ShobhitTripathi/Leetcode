# [79. Word Search](https://leetcode.com/problems/word-search/)
Medium


Given an m x n grid of characters board and a string word, return true if word exists in the grid.

The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once.

 

Example 1:
```
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
Output: true
```
Example 2:
```
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
Output: true
```
Example 3:
```
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
Output: false
``` 

Constraints:

- m == board.length
- n = board[i].length
- 1 <= m, n <= 6
- 1 <= word.length <= 15
- board and word consists of only lowercase and uppercase English letters.
 
### Follow up: Could you use search pruning to make your solution faster with a larger board?

## Approach
```
Backtracking:
- 4 choices top, down, left & right moves
- mark the element we are accessing
- if we match the word, we get the ans
```

## Solution
```java
class Solution {
    private int ROWS, COLS;
    private boolean[][] visited;

    public boolean exist(char[][] board, String word) {
        ROWS = board.length;
        COLS = board[0].length;
        visited = new boolean[ROWS][COLS];

        for (int r = 0;r < ROWS;r++) {
            for (int c = 0;c < COLS;c++) {
                if (dfs(board, word, r, c, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int r, int c, int i) {
        if (i == word.length()) {
            return true;
        }

        if (r < 0 || c < 0 || r >= ROWS || c >= COLS || 
            board[r][c] != word.charAt(i) || visited[r][c]) {
                return false;
            }

            visited[r][c] = true;
            boolean res = dfs(board, word, r + 1, c, i + 1) ||
                        dfs(board, word, r - 1, c, i + 1) ||
                        dfs(board, word, r, c + 1, i + 1) ||
                        dfs(board, word, r, c - 1, i + 1);
            visited[r][c] = false;

            return res;
    }
}


## Complexity Analysis
```
- Time Complexity: O(m * n * 4^N) [4^N for abck tracting directions]
- Space Complexity: O(m * n + L) L-> stack space
```
