Problem Statement
```
2559. Count Vowel Strings in Ranges

You are given a 0-indexed array of strings words and a 2D array of integers queries.

Each query queries[i] = [li, ri] asks us to find the number of strings present at the indices ranging from li to ri (both inclusive) of words that start and end with a vowel.

Return an array ans of size queries.length, where ans[i] is the answer to the ith query.

Note that the vowel letters are 'a', 'e', 'i', 'o', and 'u'.

 

Example 1:

Input: words = ["aba","bcb","ece","aa","e"], queries = [[0,2],[1,4],[1,1]]
Output: [2,3,0]
Explanation: The strings starting and ending with a vowel are "aba", "ece", "aa" and "e".
The answer to the query [0,2] is 2 (strings "aba" and "ece").
to query [1,4] is 3 (strings "ece", "aa", "e").
to query [1,1] is 0.
We return [2,3,0].

Example 2:

Input: words = ["a","e","i"], queries = [[0,2],[0,1],[2,2]]
Output: [3,2,1]
Explanation: Every string satisfies the conditions, so we return [3,2,1].
```

Algorithm
```
Declare our answer array ans.
Initialize our set of vowels vowels to contain the vowel list [a, e, i, o, u].
Declare our prefix sum array prefixSum to store the cumulative sum of vowel words up to each index.
To fill in prefixSum, loop through each word in words:
For each word, check if the first and last letter of word is in vowels. If so, we have found a new vowel string so we increment sum++.
Fill in the prefix count: prefixSum[i] = sum
Loop through each query in queries:
- Check if the left bound queries[i][0] is 0. If it is, then the answer is simply the cumulative count of vowel strings up to index i: ans[i] = prefixSum[queries[i][1]]
- Otherwise, ans[i] = prefixSum[queries[i][1]] - prefixSum[queries[i][0] - 1]
Return answer array ans containing answers for all queries.
```


Solution
```java

class Solution {

    public int[] vowelStrings(String[] words, int[][] queries) {
        int[] ans = new int[queries.length];
        HashSet<Character> vowels = new HashSet<>(
            Arrays.asList('a', 'e', 'i', 'o', 'u')
        );
        int[] prefixSum = new int[words.length];
        int sum = 0;
        for (int i = 0; i < words.length; i++) {
            String currentWord = words[i];
            if (
                vowels.contains(currentWord.charAt(0)) &&
                vowels.contains(currentWord.charAt(currentWord.length() - 1))
            ) {
                sum++;
            }
            prefixSum[i] = sum;
        }

        for (int i = 0; i < queries.length; i++) {
            int[] currentQuery = queries[i];
            ans[i] =
                prefixSum[currentQuery[1]] -
                (currentQuery[0] == 0 ? 0 : prefixSum[currentQuery[0] - 1]);
        }

        return ans;
    }
}

```
