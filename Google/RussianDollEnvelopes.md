# 354. Russian Doll Envelopes

You are given a 2D array of integers envelopes where envelopes[i] = [wi, hi] represents the width and the height of an envelope.

One envelope can fit into another if and only if both the width and height of one envelope are greater than the other envelope's width and height.

Return the maximum number of envelopes you can Russian doll (i.e., put one inside the other).

Note: You cannot rotate an envelope.

 Example 1:
```
Input: envelopes = [[5,4],[6,4],[6,7],[2,3]]
Output: 3
Explanation: The maximum number of envelopes you can Russian doll is 3 ([2,3] => [5,4] => [6,7]).
```
Example 2:
```
Input: envelopes = [[1,1],[1,1],[1,1]]
Output: 1
```

Constraints:
```
1 <= envelopes.length <= 105
envelopes[i].length == 2
1 <= wi, hi <= 105
```

```approach
Sort + Longest Increasing Subsequence

Algorithm
We answer the question from the intuition by sorting.
Let's pretend that we found the best arrangement of envelopes.
We know that each envelope must be increasing in w, thus our best arrangement has to be a subsequence of all our envelopes sorted on w.

After we sort our envelopes, we can simply find the length of the longest increasing subsequence on the second dimension (h).
Note that we use a clever trick to solve some edge cases:

Consider an input [[1, 3], [1, 4], [1, 5], [2, 3]].
If we simply sort and extract the second dimension we get [3, 4, 5, 3], which implies that we can fit three envelopes (3, 4, 5).
The problem is that we can only fit one envelope, since envelopes that are equal in the first dimension can't be put into each other.

In order fix this, we don't just sort increasing in the first dimension - we also sort decreasing on the second dimension,
so two envelopes that are equal in the first dimension can never be in the same increasing subsequence.

Now when we sort and extract the second element from the input we get [5, 4, 3, 3], which correctly reflects an LIS of one.
```


```java
// O(N Log N)

import java.util.*;

class Solution {

    // Standard LIS implementation using patience sorting idea (O(n log n))
    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];  // dp[i] = smallest possible tail of an LIS of length (i+1)
        int len = 0;  // length of LIS found so far

        for (int num : nums) {
            // Binary search in dp[0..len) to find insertion point for num
            int i = Arrays.binarySearch(dp, 0, len, num);

            if (i < 0) {
                // If not found, binarySearch returns (-(insertionPoint) - 1)
                // Recover insertion index:
                i = -(i + 1);
            }

            // Place/replace element in dp
            dp[i] = num;

            // If inserted at the end, we extended LIS by 1
            if (i == len) {
                len++;
            }
        }
        return len;
    }

    public int maxEnvelopes(int[][] envelopes) {
        // Step 1: Sort envelopes
        // Rule:
        //   - Ascending by width
        //   - If widths are equal, descending by height
        //     (to prevent counting envelopes with same width multiple times)
        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] == b[0]) {
                return b[1] - a[1];  // descending order by height
            } else {
                return a[0] - b[0];  // ascending order by width
            }
        });

        // Step 2: Extract the second dimension (heights)
        int[] secondDim = new int[envelopes.length];
        for (int i = 0; i < envelopes.length; ++i) {
            secondDim[i] = envelopes[i][1];
        }

        // Step 3: Apply LIS on heights
        // Since widths are already sorted (and equal widths handled),
        // LIS on heights gives us the max envelopes we can "nest"
        return lengthOfLIS(secondDim);
    }
}

```
