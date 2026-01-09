# 418. Sentence Screen Fitting

Given a rows x cols screen and a sentence represented as a list of strings, 
return the number of times the given sentence can be fitted on the screen.

The order of words in the sentence must remain unchanged, and a word cannot be split into two lines.
A single space must separate two consecutive words in a line.

 Example 1:
```
Input: sentence = ["hello","world"], rows = 2, cols = 8
Output: 1
Explanation:
hello---
world---
The character '-' signifies an empty space on the screen.
```
Example 2:
```
Input: sentence = ["a", "bcd", "e"], rows = 3, cols = 6
Output: 2
Explanation:
a-bcd- 
e-a---
bcd-e-
The character '-' signifies an empty space on the screen.
```
Example 3:
```
Input: sentence = ["i","had","apple","pie"], rows = 4, cols = 5
Output: 1
Explanation:
i-had
apple
pie-i
had--
The character '-' signifies an empty space on the screen.
```
 

Constraints:
```
1 <= sentence.length <= 100
1 <= sentence[i].length <= 10
sentence[i] consists of lowercase English letters.
1 <= rows, cols <= 2 * 104
```

Approach
```
“I flatten the sentence into a cyclic string with spaces.
For each row, I try to place cols characters.
If it ends mid-word, I roll back to the nearest space.
Total fitted characters divided by sentence length gives the answer.”


DP optimization
“Instead of recalculating word fitting for every row,
 I precompute for each starting word how many words fit in one row and reuse that via DP,
reducing time to O(rows).”
```

Time & Space Complexity
```
Time: O(rows × maxWordLength) → effectively O(rows)
Space: O(total sentence length)
```

Solution
```java
class Solution {
    public int wordsTyping(String[] sentence, int rows, int cols) {
        // Step 1: Build the full sentence string with trailing space
        String s = String.join("-", sentence) + "-";
        int n = s.length();

        int pos = 0; // total characters fitted so far

        // Step 2: Process each row
        for (int r = 0; r < rows; r++) {
            pos += cols;

            // If we land in the middle of a word, rollback
            while (pos > 0 && s.charAt(pos % n) != '-') {
                pos--;
            }

            // Move past the space to start next word
            pos++;
        }

        // Step 3: Number of full sentence fits
        return pos / n;
    }
}
```

DP Solution
```
class Solution {
    public int wordsTyping(String[] sentence, int rows, int cols) {
        int n = sentence.length;

        // dp[i]  -> how many words can fit in ONE row
        //           if the row starts with sentence[i]
        int[] dp = new int[n];

        // next[i] -> index of the word where the NEXT row will start
        int[] next = new int[n];

        // Precompute dp and next for each possible starting word
        for (int i = 0; i < n; i++) {
            int currLen = 0;   // characters used in current row
            int j = i;         // pointer to current word
            int count = 0;     // words fitted in this row

            // Try placing words until column limit is exceeded
            while (currLen + sentence[j].length() <= cols) {
                currLen += sentence[j].length() + 1; // +1 for space
                j = (j + 1) % n;                     // wrap around sentence
                count++;
            }

            dp[i] = count;  // total words placed starting from i
            next[i] = j;    // next row starts from this word index
        }

        int idx = 0;         // starting word index for current row
        int totalWords = 0;  // total words placed on the screen

        // Build the screen row by row using precomputed DP
        for (int r = 0; r < rows; r++) {
            totalWords += dp[idx]; // add words fitted in this row
            idx = next[idx];       // move to starting index of next row
        }

        // Each full sentence has 'n' words
        return totalWords / n;
    }
}


```

Complexity Comparison
```
Approach	-: Time	Space
Brute force-: 	O(rows × sentence length)	O(1)
String simulation	 -: O(rows)	O(sentence length)
DP optimized -: O(n + rows)	O(n)

Where n = number of words.
```
