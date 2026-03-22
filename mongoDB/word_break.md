# [139. Word Break](https://leetcode.com/problems/word-break/)
Medium


Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.

Note that the same word in the dictionary may be reused multiple times in the segmentation.

 

Example 1:
```
Input: s = "leetcode", wordDict = ["leet","code"]
Output: true
Explanation: Return true because "leetcode" can be segmented as "leet code".
```
Example 2:
```
Input: s = "applepenapple", wordDict = ["apple","pen"]
Output: true
Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
Note that you are allowed to reuse a dictionary word.
```
Example 3:
```
Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
Output: false
 ```

Constraints:

- 1 <= s.length <= 300
- 1 <= wordDict.length <= 1000
- 1 <= wordDict[i].length <= 20
- s and wordDict[i] consist of only lowercase English letters.
- All the strings of wordDict are unique.

## Approach
### Decision tree of Backtracking
![image](https://user-images.githubusercontent.com/20329508/177688399-84392e46-3da0-4d19-9228-fb952ef77022.png)

### Cache
![image](https://user-images.githubusercontent.com/20329508/177688529-5ba87230-2455-4680-a805-650652005975.png)

### Bottom up
![image](https://user-images.githubusercontent.com/20329508/177689013-929cba9b-8767-46e4-a5b7-c2186727b258.png)

```
Memoization Initialization:
 Use a HashMap (memo) to store previously computed results for substrings starting at each index.
 Initially, set memo[s.length()] = true since reaching the end of the string means a valid segmentation.

DFS Function:
 The recursive dfs function checks if the substring starting at index i can be segmented.
 Base Case: If i is already in memo, return the stored result (avoid recomputation).

Word Matching:
 Iterate through all words in the wordDict.
 For each word:
  Check if the substring from i matches the word and does not exceed the string's length.
  If a match is found, recursively call dfs for the next starting index (i + w.length()).

Return True on Success:
 If any recursive call returns true, the current substring can be segmented. Store true in memo for index i and return true.

Return False on Failure:
 If no match leads to a successful segmentation, store false in memo for index i and return false.
```

## Solution
```java
class Solution {
    Map<Integer, Boolean> memo;  // Memoization map to store results for indices

    public boolean wordBreak(String s, List<String> wordDict) {
        memo = new HashMap<>();
        memo.put(s.length(), true);  // Base case: An empty string can always be segmented
        return dfs(s, wordDict, 0);
    }

    private boolean dfs(String s, List<String> wordDict, int i) {
        // Return cached result if already computed
        if (memo.containsKey(i)) {
            return memo.get(i);
        }

        // Try matching every word in the dictionary
        for (String w : wordDict) {
            // Check if the substring matches the word
            if (i + w.length() <= s.length() &&
                s.substring(i, i + w.length()).equals(w)) {
                    // Recur for the remaining string after the matched word
                    if (dfs(s, wordDict, i + w.length())) {
                        memo.put(i, true);  // Cache result as true
                        return true;
                    }
                }
        }

        // If no match is found, cache and return false
        memo.put(i, false);
        return false;
    }
}

```

## Complexity Analysis
```
- Time Complexity: O(N*len(wordInDict))
- Space Complexity: O(N)
```
