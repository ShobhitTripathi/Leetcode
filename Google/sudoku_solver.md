# Sudoku Solver
Write a program to solve a Sudoku puzzle by filling the empty cells.

A sudoku solution must satisfy all of the following rules:

Each of the digits 1-9 must occur exactly once in each row.
Each of the digits 1-9 must occur exactly once in each column.
Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
The '.' character indicates empty cells.

Example 1:
```
Input: board = [["5","3",".",".","7",".",".",".","."],["6",".",".","1","9","5",".",".","."],[".","9","8",".",".",".",".","6","."],["8",".",".",".","6",".",".",".","3"],["4",".",".","8",".","3",".",".","1"],["7",".",".",".","2",".",".",".","6"],[".","6",".",".",".",".","2","8","."],[".",".",".","4","1","9",".",".","5"],[".",".",".",".","8",".",".","7","9"]]
Output: [["5","3","4","6","7","8","9","1","2"],["6","7","2","1","9","5","3","4","8"],["1","9","8","3","4","2","5","6","7"],["8","5","9","7","6","1","4","2","3"],["4","2","6","8","5","3","7","9","1"],["7","1","3","9","2","4","8","5","6"],["9","6","1","5","3","7","2","8","4"],["2","8","7","4","1","9","6","3","5"],["3","4","5","2","8","6","1","7","9"]]
Explanation: The input board is shown above and the only valid solution is shown below
```

Constraints:
```
board.length == 9
board[i].length == 9
board[i][j] is a digit or '.'.
It is guaranteed that the input board has only one solution.
```


Approach
```
Approach (Backtracking + Constraint Propagation)

Start from the first empty cell.

For each number 1–9, check if it is valid in the current row, column, and 3×3 sub-box.

Sub-box index can be computed as:
  boxIndex=(row / 3) × 3 + col / 3;

If valid:
Place the number and mark it as used in row, column, and sub-box.
Recurse to solve the next empty cell.
If the choice leads to a dead end → backtrack (undo the placement, try another number).
Stop when the entire board is filled.

Complexity Analysis
Time Complexity:
  Worst case ≈ O((9!)^9) → but pruning (constraint propagation) makes it much faster in practice.
Effectively, much better than brute force O(9^81).

Space Complexity:
Board + bookkeeping arrays for rows, columns, boxes → O(81) = O(1) (constant, since board size is fixed).

In short: Backtracking + constraint propagation systematically reduces possibilities,
solving Sudoku efficiently in practice despite worst-case exponential complexity.
```

Solution
```java
class Solution {
    // box size
    int n = 3;
    // row size
    int N = n * n;

    int[][] rows = new int[N][N + 1];
    int[][] columns = new int[N][N + 1];
    int[][] boxes = new int[N][N + 1];

    char[][] board;
    boolean sudokuSolved = false;
    
    /* Check if one could place a number d in (row, col) cell */
    public boolean couldPlace(int d, int row, int col) {
        int idx = (row / n) * n + col / n;
        return rows[row][d] + columns[col][d] + boxes[idx][d] == 0;
    }

    /* Place a number d int (row, col) cell */
    public void placeNumber(int d, int row, int col) {
        int idx = (row / n) * n + col / n;

        rows[row][d]++;
        columns[col][d]++;
        boxes[idx][d]++;
        board[row][col] = (char) (d + '0');
    }

    /* Remove number that didn't lead to a solution */
    public void removeNumber(int d, int row, int col) {
        int idx = (row / n) * n + col / n;
        rows[row][d]--;
        columns[col][d]--;
        boxes[idx][d]--;
        board[row][col] = '.';
    }

    /* Call backtrack function in recursion 
        to continue to place numbers
        till the moment we have a solution
    */
    public void placeNextNumbers(int row, int col) {
        // if we're in the last cell
        // that means we have the solution
        if ((row == N - 1) && (col == N - 1)) {
            sudokuSolved = true;
        } else {
            if (col == N - 1) backtrack(row + 1, 0);
            else backtrack(row, col + 1);
        }
    }

    /* backtrackinh */
    public void backtrack(int row, int col) {
        // if the cell is empty
        if (board[row][col] == '.') {
            for (int d = 1;d < 10;d++) {
                if (couldPlace(d, row, col)) {
                    placeNumber(d, row, col);
                    placeNextNumbers(row, col);

                    // if sudoku is solved, there is no need to backtrack
                    // since single unique solution is promised
                    if (!sudokuSolved) removeNumber(d, row, col);
                }
            }
        } else {
            placeNextNumbers(row, col);
        }
    }

    public void solveSudoku(char[][] board) {
        this.board = board;

        // init rows, columns, and boxes
        for (int i = 0;i < N;i++) {
            for (int j = 0;j < N;j++) {
                char num = board[i][j];
                if (num != '.') {
                    int d = Character.getNumericValue(num);
                    placeNumber(d, i, j);
                }
            }
        }
        backtrack(0, 0);
    }
}
```
