Problem Statement
```
2639. Find the Width of Columns of a Grid

You are given a 0-indexed m x n integer matrix grid. The width of a column is the maximum length of its integers.

For example, if grid = [[-10], [3], [12]], the width of the only column is 3 since -10 is of length 3.
Return an integer array ans of size n where ans[i] is the width of the ith column.

The length of an integer x with len digits is equal to len if x is non-negative, and len + 1 otherwise.

 

Example 1:

Input: grid = [[1],[22],[333]]
Output: [3]
Explanation: In the 0th column, 333 is of length 3.
Example 2:

Input: grid = [[-15,1,3],[15,7,12],[5,6,-2]]
Output: [3,1,2]
Explanation: 
In the 0th column, only -15 is of length 3.
In the 1st column, all integers are of length 1. 
In the 2nd column, both 12 and -2 are of length 2.

```

Approach
```
Variable m stores the number of rows in grid
Variable n stores the number of columns in grid
Variable rez is an array where each index stores the maximum width for the corresponding column
Iterate through each row i in grid
Iterate through each column j in the current row
Call getLen with grid[i][j] to calculate the number of characters needed to represent that integer
If rez[j] is less than len update rez[j] to len
Return rez containing the maximum width for each column
getLen determines the character length of a given integer num
len is initialized to 1 if num is <= 0 (to account for the - sign or the single 0 digit) otherwise to 0
Iterate while num != 0 dividing num by 10 on each iteration and incrementing len
Return len as the total number of characters required to display the number
```

Complexity
```
Time complexity:
O(m × n × d) - We traverse every element in grid and process up to d digits for each number

Space complexity:
O(n) - The rez array stores one width value for each column

```


Solution
```java
class Solution {
    public int[] findColumnWidth(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] rez = new int[n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                int len = getLen(grid[i][j]);

                if (rez[j] < len) {
                    rez[j] = len;
                }

            }
        }

        return rez;
    }

    private int getLen(int num) {
        int len = num <= 0 ? 1 : 0;

        while (num != 0) {
            num /= 10;
            len++;
        }

        return len;
    }
}

```
