Problem Statement
```
1307. Verbal Arithmetic Puzzle

Given an equation, represented by words on the left side and the result on the right side.

You need to check if the equation is solvable under the following rules:

Each character is decoded as one digit (0 - 9).
No two characters can map to the same digit.
Each words[i] and result are decoded as one number without leading zeros.
Sum of numbers on the left side (words) will equal to the number on the right side (result).
Return true if the equation is solvable, otherwise return false.

 

Example 1:

Input: words = ["SEND","MORE"], result = "MONEY"
Output: true
Explanation: Map 'S'-> 9, 'E'->5, 'N'->6, 'D'->7, 'M'->1, 'O'->0, 'R'->8, 'Y'->'2'
Such that: "SEND" + "MORE" = "MONEY" ,  9567 + 1085 = 10652
Example 2:

Input: words = ["SIX","SEVEN","SEVEN"], result = "TWENTY"
Output: true
Explanation: Map 'S'-> 6, 'I'->5, 'X'->0, 'E'->8, 'V'->7, 'N'->2, 'T'->1, 'W'->'3', 'Y'->4
Such that: "SIX" + "SEVEN" + "SEVEN" = "TWENTY" ,  650 + 68782 + 68782 = 138214
Example 3:

Input: words = ["LEET","CODE"], result = "POINT"
Output: false
Explanation: There is no possible mapping to satisfy the equation, so we return false.
Note that two different characters cannot map to the same digit.
```

Approach
```
Consider:
  SEND
+ MORE
------
 MONEY

We process the numbers from right to left:
1. Start with the rightmost column.
2. Assign digits to the characters in that column.
3. Calculate their sum along with the carry.
4. The resulting digit must match the corresponding character in the result.
5. If it doesn't match, backtrack and try another digit.
6. Continue to the next column until all columns are processed.

Important conditions:
- Each character must have a unique digit.
- A word cannot start with zero.
- Carry must be maintained between columns.
- The final carry must also match the remaining result.

```


Solution
```java

class Solution {

    private String[] words;
    private String result;
    private int[] map = new int[26];
    private boolean[] used = new boolean[10];
    private boolean[] leading = new boolean[26];

    public boolean isSolvable(String[] words, String result) {

        this.words = words;
        this.result = result;

        // Initialize all character mappings as unassigned
        java.util.Arrays.fill(map, -1);

        // Identify characters that cannot be zero
        for (String word : words) {
            if (word.length() > 1) {
                leading[word.charAt(0) - 'A'] = true;
            }
        }

        if (result.length() > 1) {
            leading[result.charAt(0) - 'A'] = true;
        }

        // Result cannot be shorter than the longest input word
        int maxLen = result.length();

        for (String word : words) {
            maxLen = Math.max(maxLen, word.length());
        }

        if (result.length() < maxLen ||
            result.length() > maxLen + 1) {
            return false;
        }

        return solve(0, 0, 0);
    }

    private boolean solve(int col, int row, int sum) {

        // All columns processed
        if (col == result.length()) {
            return row == 0 && sum == 0;
        }

        // Process all input words in the current column
        if (row < words.length) {

            String word = words[row];

            if (col >= word.length()) {
                return solve(col, row + 1, sum);
            }

            int ch = word.charAt(word.length() - 1 - col) - 'A';

            // Character already has a digit
            if (map[ch] != -1) {
                return solve(col, row + 1, sum + map[ch]);
            }

            // Try assigning an unused digit
            for (int d = 0; d <= 9; d++) {

                if (used[d] || (d == 0 && leading[ch])) {
                    continue;
                }

                map[ch] = d;
                used[d] = true;

                if (solve(col, row + 1, sum + d)) {
                    return true;
                }

                // Backtrack
                map[ch] = -1;
                used[d] = false;
            }

            return false;
        }

        // All input words processed; calculate result digit
        int digit = sum % 10;
        int carry = sum / 10;

        if (col >= result.length()) {
            return false;
        }

        int ch = result.charAt(result.length() - 1 - col) - 'A';

        // Result character already has a digit
        if (map[ch] != -1) {
            if (map[ch] != digit) {
                return false;
            }

            return solve(col + 1, 0, carry);
        }

        // Assign the required result digit
        if (used[digit] || (digit == 0 && leading[ch])) {
            return false;
        }

        map[ch] = digit;
        used[digit] = true;

        if (solve(col + 1, 0, carry)) {
            return true;
        }

        // Backtrack
        map[ch] = -1;
        used[digit] = false;

        return false;
    }
}

```

Interview Summary
```

I use backtracking with column-wise addition. Instead of assigning digits to all characters and validating the equation at the end, I process each column from right to left.

For every column, I assign unused digits to unassigned characters, calculate the sum along with the carry, and verify that the resulting digit matches the corresponding result character.

If the digit doesn't match or violates the uniqueness/leading-zero constraint, I backtrack immediately. This helps prune invalid combinations early and avoids exploring unnecessary assignments.

```
