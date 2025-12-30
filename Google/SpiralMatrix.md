# 54. Spiral Matrix

Given an m x n matrix, return all elements of the matrix in spiral order.

Example 1:
```
Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [1,2,3,6,9,8,7,4,5]
```
Example 2:
```
Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
Output: [1,2,3,4,8,12,11,10,9,5,6,7]
 ```

Constraints:
```
m == matrix.length
n == matrix[i].length
1 <= m, n <= 10
-100 <= matrix[i][j] <= 100
```

Algorithm
```
Initialize the top, right, bottom, and left boundaries as up, right, down, and left.
Initialize the output array result.
Traverse the elements in spiral order and add each element to result:
Traverse from left boundary to right boundary.
Traverse from up boundary to down boundary.
Before we traverse from right to left, we need to make sure that we are not on a row that has already been traversed. If we are not, then we can traverse from right to left.
Similarly, before we traverse from top to bottom, we need to make sure that we are not on a column that has already been traversed. Then we can traverse from down to up.
Remember to move the boundaries by updating left, right, up, and down accordingly.
Return result.
```


Solution
```java
class Solution {
    private int ROWS, COLS;

    public List<Integer> spiralOrder(int[][] matrix) {
        ROWS = matrix.length;
        COLS = matrix[0].length;
        List<Integer> result = new ArrayList<>();
        int up = 0, left = 0, right = COLS - 1, down = ROWS - 1;

        while (result.size() < ROWS * COLS) {
            // Traverse from left to right
            for (int c = left; c <= right;c++) {
                result.add(matrix[up][c]);
            }

            // Traverse downwards
            for (int r = up + 1;r <= down;r++) {
                result.add(matrix[r][right]);
            }

            // Make sure we are now on a different row.
            if (up != down) {
                // Traverse from right to left.
                for (int c = right - 1; c >= left;c--) {
                    result.add(matrix[down][c]);
                }
            }

            // Make sure we are now on a different column.
            if (left != right) {
                // Traverse upwards
                for (int r = down - 1;r > up;r--) {
                    result.add(matrix[r][left]);
                }
            }

            left++;
            right--;
            up++;
            down--;
        }



        return result;
    }
}
```
