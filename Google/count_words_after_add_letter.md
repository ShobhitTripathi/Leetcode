# 2135. Count Words Obtained After Adding a Letter

You are given two 0-indexed arrays of strings startWords and targetWords. Each string consists of lowercase English letters only.

For each string in targetWords, check if it is possible to choose a string from startWords and perform a conversion operation on it to be equal to that from targetWords.

The conversion operation is described in the following two steps:

Append any lowercase letter that is not present in the string to its end.
For example, if the string is "abc", the letters 'd', 'e', or 'y' can be added to it, but not 'a'. If 'd' is added, the resulting string will be "abcd".
Rearrange the letters of the new string in any arbitrary order.
For example, "abcd" can be rearranged to "acbd", "bacd", "cbda", and so on. Note that it can also be rearranged to "abcd" itself.
Return the number of strings in targetWords that can be obtained by performing the operations on any string of startWords.

Note that you will only be verifying if the string in targetWords can be obtained from a string in startWords by performing the operations.
The strings in startWords do not actually change during this process.

Example 1:
```
Input: startWords = ["ant","act","tack"], targetWords = ["tack","act","acti"]
Output: 2
Explanation:
- In order to form targetWords[0] = "tack", we use startWords[1] = "act", append 'k' to it, and rearrange "actk" to "tack".
- There is no string in startWords that can be used to obtain targetWords[1] = "act".
  Note that "act" does exist in startWords, but we must append one letter to the string before rearranging it.
- In order to form targetWords[2] = "acti", we use startWords[1] = "act", append 'i' to it, and rearrange "acti" to "acti" itself.
```
Example 2:
```
Input: startWords = ["ab","a"], targetWords = ["abc","abcd"]
Output: 1
Explanation:
- In order to form targetWords[0] = "abc", we use startWords[0] = "ab", add 'c' to it, and rearrange it to "abc".
- There is no string in startWords that can be used to obtain targetWords[1] = "abcd".
 ```

Constraints:
```
1 <= startWords.length, targetWords.length <= 5 * 104
1 <= startWords[i].length, targetWords[j].length <= 26
Each string of startWords and targetWords consists of lowercase English letters only.
```

Approach
```
Store sorted start words in a HashSet;
for each target word,
  remove one character at a time from its sorted form and check for existence

```

Solution
```java
class Solution {
    public int wordCount(String[] startWords, String[] targetWords) {

        // HashSet to store canonical (sorted) representations of startWords
        Set<String> set = new HashSet<>();

        // Step 1:
        // Sort characters of each startWord and store in the set.
        // This removes dependency on character order.
        for (String start : startWords) {
            char[] sArr = start.toCharArray();
            Arrays.sort(sArr);
            set.add(new String(sArr));
        }

        int count = 0;

        // Step 2:
        // For each targetWord, sort its characters
        // and try removing exactly one character
        for (String target : targetWords) {
            char[] tArr = target.toCharArray();
            Arrays.sort(tArr);
            String sortedTarget = new String(tArr);

            // Step 3:
            // Remove one character at every position and check
            // if the remaining string exists in startWords set
            for (int i = 0; i < sortedTarget.length(); i++) {

                // Create a candidate string by omitting character at index i
                String candidate =
                        sortedTarget.substring(0, i) +
                        sortedTarget.substring(i + 1);

                // If a valid startWord is found, count it and move to next targetWord
                if (set.contains(candidate)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }
}

```

Complexity Analysis
```
Time Complexity
O(S · L log L + T · L log L + T · L²)
Where:
S = number of startWords
T = number of targetWords
L = max word length (≤ 26)
Since L is small, this runs efficiently.

Space Complexity
O(S · L)
HashSet stores sorted startWords.

No letter occurs more than once in any string of startWords or targetWords.
```
