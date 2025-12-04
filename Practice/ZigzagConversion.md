# 6. Zigzag Conversion

The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this: (you may want to display this pattern in a fixed font for better legibility)

P   A   H   N
A P L S I I G
Y   I   R
And then read line by line: "PAHNAPLSIIGYIR"

Write the code that will take a string and make this conversion given a number of rows:

string convert(string s, int numRows);
 

Example 1:
```
Input: s = "PAYPALISHIRING", numRows = 3
Output: "PAHNAPLSIIGYIR"
```
Example 2:
```
Input: s = "PAYPALISHIRING", numRows = 4
Output: "PINALSIGYAHRPI"
Explanation:
P     I    N
A   L S  I G
Y A   H R
P     I
```
Example 3:
```
Input: s = "A", numRows = 1
Output: "A"
 ```

Constraints:
```
1 <= s.length <= 1000
s consists of English letters (lower-case and upper-case), ',' and '.'.
1 <= numRows <= 1000
```

Approach 
```
We compute how many columns the zigzag would require,
create a matrix of size numRows × numCols,
and simulate the zigzag movement: first going straight down, then diagonally up.

We place characters in this path until the string ends.

Finally, we read the matrix row-wise to build the output string.
```

Solution
```java
// Time: O(n + numRows*numCols) → effectively O(n) Space: O(numRows * numCols) → worst-case O(n)
class Solution {
    public String convert(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }

        int n = s.length();
        int sections = (int) Math.ceil(n / (2 * numRows - 2.0));
        int numCols = sections * (numRows - 1);

        char[][] matrix = new char[numRows][numCols];
        for (char[] row : matrix) {
            Arrays.fill(row, ' ');
        }

        int currRow = 0, currCol = 0;
        int currStringIndex = 0;

        // Iterate in zig-zag pattern on matrix and fill it with string characters.
        while (currStringIndex < n) {
            // Move down.
            while (currRow < numRows && currStringIndex < n) {
                matrix[currRow][currCol] = s.charAt(currStringIndex);
                currRow++;
                currStringIndex++;
            }

            currRow -= 2;
            currCol++;

            // Move up (with moving right also).
            while (currRow > 0 && currCol < numCols && currStringIndex < n) {
                matrix[currRow][currCol] = s.charAt(currStringIndex);
                currRow--;
                currCol++;
                currStringIndex++;
            }
        }

        StringBuilder answer = new StringBuilder();
        for (char[] row : matrix) {
            for (char character : row) {
                if (character != ' ') {
                    answer.append(character);
                }
            }
        }

        return answer.toString();
    }
}
```
