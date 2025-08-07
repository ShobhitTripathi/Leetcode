Write a function to find the longest common prefix string amongst an array of strings.

If there is no common prefix, return an empty string "".

Example 1:
Input: strs = ["flower","flow","flight"]
Output: "fl"

Example 2:
Input: strs = ["dog","racecar","car"]
Output: ""
Explanation: There is no common prefix among the input strings.

```
Approach: Horizontal Scanning
We initialize the prefix as the first string, then compare it with the rest of the strings one by one, updating the prefix each time.

```

```java
class Solution {
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";

        String prefix = strs[0];

        for (int i = 1; i < strs.length; i++) {
            while (!strs[i].startsWith(prefix)) {
                // Trim the prefix until it matches
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }

        return prefix;
    }
}
```

```
Time Complexity:
Best case: O(S) where S is total number of characters (if all strings are similar).
Worst case: If strings differ early, it's O(N * M) where N is number of strings and M is length of the longest string.
```
