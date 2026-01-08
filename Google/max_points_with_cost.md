# 1937. Maximum Number of Points with Cost

You are given an m x n integer matrix points (0-indexed). Starting with 0 points, you want to maximize the number of points you can get from the matrix.

To gain points, you must pick one cell in each row. Picking the cell at coordinates (r, c) will add points[r][c] to your score.

However, you will lose points if you pick a cell too far from the cell that you picked in the previous row. For every two adjacent rows r and r + 1 (where 0 <= r < m - 1), picking cells at coordinates (r, c1) and (r + 1, c2) will subtract abs(c1 - c2) from your score.

Return the maximum number of points you can achieve.

abs(x) is defined as:

x for x >= 0.
-x for x < 0.
 
Example 1:
```
Input: points = [[1,2,3],[1,5,1],[3,1,1]]
Output: 9
Explanation:
The blue cells denote the optimal cells to pick, which have coordinates (0, 2), (1, 1), and (2, 0).
You add 3 + 5 + 3 = 11 to your score.
However, you must subtract abs(2 - 1) + abs(1 - 0) = 2 from your score.
Your final score is 11 - 2 = 9.
```
Example 2:
```
Input: points = [[1,5],[2,3],[4,2]]
Output: 11
Explanation:
The blue cells denote the optimal cells to pick, which have coordinates (0, 1), (1, 1), and (2, 0).
You add 5 + 3 + 4 = 12 to your score.
However, you must subtract abs(1 - 1) + abs(1 - 0) = 1 from your score.
Your final score is 12 - 1 = 11.
 ```

Constraints:
```
m == points.length
n == points[r].length
1 <= m, n <= 105
1 <= m * n <= 105
0 <= points[r][c] <= 105
```

Approach 2: Dynamic Programming (Optimized)

```
Intuition
In the previous approach, we used auxiliary arrays to keep track of the maximum points achievable from the left and right directions. 
This time, we streamline the process by using the previousRow array itself as 
temporary storage for the left-side maximums and then update it with the right-side maximums in a single pass.

Thus, we will require two passes to do this.

First Pass: Left-to-Right Sweep
We begin by iterating through the row from left to right. 
As we move, we store the maximum points achievable from the left in the previousRow array. 
This step essentially builds the equivalent of the leftMax array directly within previousRow.
At the start, runningMax is initialized to 0. At the beginning of each iteration, 
runningMax will hold the maximum value that can be achieved from the left till i-1.
For each cell i, we update runningMax to the maximum of previousRow[i] and runningMax - 1, 
where the subtraction accounts for the horizontal distance penalty.
This process ensures that previousRow[i] contains the maximum points that can be accumulated when moving from the left to the ith cell.

Second Pass: Right-to-Left Sweep
Next, we perform a second loop, this time iterating from right to left. 
This pass starts from the right and combines the results from the left-to-right pass with the maximum values from the right.
We reset runningMax to 0 before starting this pass. Similar to the left-to-right pass, we update runningMax for each column.
We take the maximum of the current previousRow[col] (which now contains the best value from the left) and the new runningMax (best value from the right).
We add row[col] to this maximum, incorporating the points from the current cell in the current row.
After processing all rows, the array previousRow (which now holds the updated values) will contain the maximum points that can be accumulated for each cell in the last row of the matrix. The maximum value in this array is our final answer, representing the highest possible score from the top to the bottom of the matrix.

Algorithm:

Set cols as the number of columns in points.
Create an array previousRow of size cols.
Iterate through each row in the points matrix:
Initialize a variable runningMax to 0.
Iterate col from 0 to cols-1:
Update runningMax to the maximum of runningMax - 1 and previousRow[col].
Set previousRow[col] equal to runningMax.
Now, iterate col in the reverse order:
Update runningMax to the maximum of runningMax - 1 and previousRow[col].
Update previousRow[col] by taking the maximum of its current value and runningMax, then add the current cell's value.
Loop through all values of previousRow and set maxPoints to the maximum.
Return maxPoints.

```
Solution
```java
class Solution {
    private int ROWS;
    private int COLS;

    public long maxPoints(int[][] points) {
        ROWS = points.length;
        COLS = points[0].length;
        long[] prevRow = new long[COLS];

        // Inititalize first row
        for (int c = 0;c < COLS;c++) {
            prevRow[c] = points[0][c];
        }

        // Process each row
        for (int r = 0;r < ROWS - 1;r++) {
            long[] leftMax = new long[COLS];
            long[] rightMax = new long[COLS];
            long[] currentRow = new long[COLS];

            // Calculate left-to-right max
            leftMax[0] = prevRow[0];
            for (int c = 1;c < COLS;c++) {
                leftMax[c] = Math.max(leftMax[c - 1] - 1, prevRow[c]);
            }

            // Calculate right-to-left maximum
            rightMax[COLS - 1] = prevRow[COLS -1];
            for (int c = COLS - 2;c >= 0;c--) {
                rightMax[c] = Math.max(rightMax[c + 1] - 1, prevRow[c]);
            }

            // Calculate the current row's maximum points
            for (int c = 0;c < COLS;c++) {
                currentRow[c] = points[r + 1][c] + Math.max(leftMax[c], rightMax[c]);
            }

            // Update prevRow for the next iteration
            prevRow = currentRow;
        }

        // Find the max value int the last processed row
        long maxPoints = 0;
        for (int c = 0;c < COLS;c++) {
            maxPoints = Math.max(maxPoints, prevRow[c]);
        }
        return maxPoints;
    }
}
```
