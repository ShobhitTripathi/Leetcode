## Problem Statement

Given two strings:

```java
str
pattern
```

Find the number of ways `pattern` can be matched as a **subsequence** in `str` such that no two selected characters are adjacent in `str`.

Example:

```text
pattern = "cat"
str = "catapult"
```

Valid match:

```text
c a t a p u l t
C     A       T
```

Output:

```text
1
```

---

## Approach

Use recursion + memoization.

At every index `i` in `str`, and index `j` in `pattern`, we have two choices:

1. Skip `str[i]`
2. If `str[i] == pattern[j]`, pick it and move to `i + 2`

We move to `i + 2` because the next selected character cannot be adjacent to the current one.

State:

```java
dp[i][j] = number of ways to match pattern[j...] using str[i...]
```

---

## Java Solution

```java
import java.util.*;

class Solution {

    public static int countMatches(String str, String pattern) {
        int n = str.length();
        int m = pattern.length();

        // dp[i][j] stores answer for str index i and pattern index j
        Integer[][] dp = new Integer[n + 2][m + 1];

        return solve(str, pattern, 0, 0, dp);
    }

    private static int solve(String str, String pattern, int i, int j, Integer[][] dp) {
        // Entire pattern matched
        if (j == pattern.length()) {
            return 1;
        }

        // String finished but pattern still remains
        if (i >= str.length()) {
            return 0;
        }

        if (dp[i][j] != null) {
            return dp[i][j];
        }

        // Option 1: skip current character
        int ways = solve(str, pattern, i + 1, j, dp);

        // Option 2: take current character if it matches pattern[j]
        if (str.charAt(i) == pattern.charAt(j)) {
            // Move to i + 2 to avoid adjacent selected characters
            ways += solve(str, pattern, i + 2, j + 1, dp);
        }

        dp[i][j] = ways;
        return ways;
    }

    public static void main(String[] args) {
        System.out.println(countMatches("catapult", "cat"));     // 1
        System.out.println(countMatches("catatapult", "cat"));   // 2
    }
}
```

## Time Complexity

```text
O(n * m)
```

where `n = str.length()` and `m = pattern.length()`.

## Space Complexity

```text
O(n * m)
```

for the memoization table.
