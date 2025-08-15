# [51. N-Queens](https://leetcode.com/problems/n-queens/)
Hard

The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.

Given an integer n, return all distinct solutions to the n-queens puzzle. You may return the answer in any order.

Each solution contains a distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate a queen and an empty space, respectively.

Example 1:
```
Input: n = 4
Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
Explanation: There exist two distinct solutions to the 4-queens puzzle as shown above
```
Example 2:
```
Input: n = 1
Output: [["Q"]]
``` 

Constraints:
- 1 <= n <= 9

## Approach
```
- Backtracking
At each row r, you try placing a queen in each column c, but only if:
The column c is not already occupied.
The diagonals r + c (positive) and r - c (negative) are also not occupied.
If placing the queen is safe, you:
Place the queen, mark the column/diagonals.
Recurse to the next row.
After recursion, you undo the queen placement to try other possibilities (backtrack).
```

## Solution
```java
class Solution {
    // Set to track which columns are already occupied by a queen
    Set<Integer> col = new HashSet<>();

    // Set to track positive diagonals (r + c) occupied by a queen
    Set<Integer> posDiag = new HashSet<>();

    // Set to track negative diagonals (r - c) occupied by a queen
    Set<Integer> negDiag = new HashSet<>();

    // Final result list to hold all valid board configurations
    List<List<String>> res = new ArrayList<>();

    public List<List<String>> solveNQueens(int n) {
        // Create an empty board filled with '.'
        char[][] board = new char[n][n];
        for (char[] row : board) {
            Arrays.fill(row, '.');
        }

        // Start backtracking from the 0th row
        backtrack(0, n, board);
        return res;
    }

    private void backtrack(int r, int n, char[][] board) {
        // Base case: all rows are filled successfully
        if (r == n) {
            List<String> copy = new ArrayList<>();
            for (char[] row : board) {
                copy.add(new String(row));
            }
            res.add(copy); // Store the valid board configuration
            return;
        }

        // Try placing a queen in every column of the current row
        for (int c = 0; c < n; c++) {
            // Skip if column or diagonals are already occupied
            if (col.contains(c) || posDiag.contains(r + c) || negDiag.contains(r - c)) {
                continue;
            }

            // Place queen and mark column and diagonals as occupied
            col.add(c);
            posDiag.add(r + c);
            negDiag.add(r - c);
            board[r][c] = 'Q';

            // Recurse to the next row
            backtrack(r + 1, n, board);

            // Backtrack: remove the queen and unmark the occupied column and diagonals
            col.remove(c);
            posDiag.remove(r + c);
            negDiag.remove(r - c);
            board[r][c] = '.';
        }
    }
}


```
## Complexity Analysis
```
- Time Complexity: O(n!)
- Space Complexity: O(n^2), building the result 
```
