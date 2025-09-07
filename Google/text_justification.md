# 68. Text Justification

Given an array of strings words and a width maxWidth, format the text such that each line has exactly maxWidth characters and is fully (left and right) justified.
You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces ' ' when necessary so that each line has exactly maxWidth characters.
Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line does not divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.
For the last line of text, it should be left-justified, and no extra space is inserted between words.

Note:
A word is defined as a character sequence consisting of non-space characters only.
Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
The input array words contains at least one word.
 

Example 1:
```
Input: words = ["This", "is", "an", "example", "of", "text", "justification."], maxWidth = 16
Output:
[
   "This    is    an",
   "example  of text",
   "justification.  "
]
```

Example 2:
```
Input: words = ["What","must","be","acknowledgment","shall","be"], maxWidth = 16
Output:
[
  "What   must   be",
  "acknowledgment  ",
  "shall be        "
]
Explanation: Note that the last line is "shall be    " instead of "shall     be", because the last line must be left-justified instead of fully-justified.
Note that the second line is also left-justified because it contains only one word.
```

Example 3:
```
Input: words = ["Science","is","what","we","understand","well","enough","to","explain","to","a","computer.","Art","is","everything","else","we","do"], maxWidth = 20
Output:
[
  "Science  is  what we",
  "understand      well",
  "enough to explain to",
  "a  computer.  Art is",
  "everything  else  we",
  "do                  "
]
```

Constraints:
```
1 <= words.length <= 300
1 <= words[i].length <= 20
words[i] consists of only English letters and symbols.
1 <= maxWidth <= 100
words[i].length <= maxWidth
```
 
Approach 
```
Approach

Build the result line by line.

  For each line:
    getWords(i): greedily pick as many words as possible that fit into maxWidth.
    createLine(line, i): format the words into a justified line:
      Normal lines → distribute extra spaces evenly (left side gets more if uneven).
      Last line or single-word line → left-justify, pad with spaces at the end.


Algorithm

Initialize i = 0, result list ans.
While i < words.length:
  Get the words for current line using getWords(i).
  Increment i by number of words used.
  Format the line using createLine(...) and add to result.
Return ans.

Complexity

Time Complexity: O(n·k)
  n = number of words
  k = average word length
Each word is processed once, and formatting costs proportional to word length + maxWidth.

Space Complexity: O(m)
  m = maxWidth (temporary line buffer).

Output not counted toward extra space.

In short: Greedy line building + spacing distribution gives clean justified text in O(n·k) time and O(m) space.

```

Solution
```java
class Solution {
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> ans = new ArrayList<>();
        int i = 0;

        while (i < words.length) {
            List<String> currentLine = getWords(i, words, maxWidth);
            i += currentLine.size();
            ans.add(createLine(currentLine, i, words, maxWidth));
        }

        return ans;
    }

    private List<String> getWords(int i, String[] words, int maxWidth) {
        List<String> currentLine = new ArrayList<>();
        int currLength = 0;

        while (i < words.length && currLength + words[i].length() <= maxWidth) {
            currentLine.add(words[i]);
            currLength += words[i].length() + 1;
            i++;
        }

        return currentLine;
    }

    private String createLine(List<String> line, int i, String[] words, int maxWidth) {
        int baseLength = -1;
        for (String word : line) {
            baseLength += word.length() + 1;
        }

        int extraSpaces = maxWidth - baseLength;

        if (line.size() == 1 || i == words.length) {
            return String.join(" ", line) + " ".repeat(extraSpaces);
        }

        int wordCount = line.size() - 1;
        int spacesPerWord = extraSpaces / wordCount;
        int needsExtraSpace = extraSpaces % wordCount;

        for (int j = 0; j < needsExtraSpace; j++) {
            line.set(j, line.get(j) + " ");
        }

        for (int j = 0; j < wordCount; j++) {
            line.set(j, line.get(j) + " ".repeat(spacesPerWord));
        }

        return String.join(" ", line);
    }
}
```
