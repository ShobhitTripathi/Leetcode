Problem statement

```
Given a string s and a dictionary of strings wordDict, add spaces in s to construct a sentence where each word is a valid dictionary word.
Return all such possible sentences in any order.

Note that the same word in the dictionary may be reused multiple times in the segmentation.

Example 1:
Input: s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
Output: ["cats and dog","cat sand dog"]

Example 2:
Input: s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]
Output: ["pine apple pen apple","pineapple pen apple","pine applepen apple"]
Explanation: Note that you are allowed to reuse a dictionary word.

Example 3:
Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
Output: []
```

Approach 
```
---
 Brute Force → Backtracking
> “Try every possible split and see what works”
### Flow
* Start at index `0`
* Try all substrings `s[start...end]`
* If valid → recurse
* If reach end → store sentence
### Problem
* Recomputes same substring multiple times
* Exponential explosion
### Complexity
* Time: `O(n * 2^n)`
* Space: `O(2^n)`

---

Optimization → Memoization (Top-down DP)
**Thinking shift:**
> “Why recompute same suffix again and again?”
### Key Idea
* Cache results of `remainingString`
* If already solved → reuse
### Structure
```java
Map<String, List<String>> memo;
### Benefit
* Avoids redundant recursion
* Each substring solved once
### Still expensive?
Yes — because:
* You STILL need to generate all valid sentences

### Complexity
* Time: `O(n * 2^n)`
* But practically much faster than backtracking

---

Bottom-up DP (Tabulation)
**Thinking shift:**
> “Instead of recursion, build answers from back”
### Key Idea
* `dp[i]` = all sentences possible from index `i`
### Flow
* Start from end
* For each `i`, try all `j > i`
* If `s[i:j]` is valid:
  * Combine with `dp[j]`
### Why better?
* No recursion overhead
* More structured computation

---

Trie Optimization (Advanced refinement)
**Thinking shift:**
> “Substring lookup is costly — can we optimize that?”
### Problem in previous approaches:
```java
substring + hash lookup → O(n)
### Solution:
* Build **Trie from dictionary**
* Traverse string directly without substring creation
### Benefit
* Faster prefix checks
* Avoid unnecessary substring operations

---

# ⚠️ Key Insight (Very Important)
Even after all optimizations:
👉 **You cannot beat `O(n * 2^n)`**
Why?
Because:
* Output itself can be exponential
* Example: `"aaaaaaa"` → every split is valid 

---

# 🧩 Final Mental Model
Think in layers:
Brute Force
   ↓ (remove recomputation)
Memoization (Top-down DP)
   ↓ (remove recursion overhead)
Tabulation (Bottom-up DP)
   ↓ (optimize lookup)
Trie + DP

```

Solutions

<img width="1124" height="1005" alt="image" src="https://github.com/user-attachments/assets/d4cc57c1-a806-487e-b75e-ee463cad6031" />

Bcktracking Solution
```java
// Time :  O(n⋅2^n), Space : O(2^n)
class Solution {

    public List<String> wordBreak(String s, List<String> wordDict) {
        // Convert wordDict to a set for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordDict);
        List<String> results = new ArrayList<>();
        // Start the backtracking process
        backtrack(s, wordSet, new StringBuilder(), results, 0);
        return results;
    }

    private void backtrack(
        String s,
        Set<String> wordSet,
        StringBuilder currentSentence,
        List<String> results,
        int startIndex
    ) {
        // If we've reached the end of the string, add the current sentence to results
        if (startIndex == s.length()) {
            results.add(currentSentence.toString().trim());
            return;
        }

        // Iterate over possible end indices
        for (
            int endIndex = startIndex + 1;
            endIndex <= s.length();
            endIndex++
        ) {
            String word = s.substring(startIndex, endIndex);
            // If the word is in the set, proceed with backtracking
            if (wordSet.contains(word)) {
                int currentLength = currentSentence.length();
                currentSentence.append(word).append(" ");
                // Recursively call backtrack with the new end index
                backtrack(s, wordSet, currentSentence, results, endIndex);
                // Reset currentSentence to its original length
                currentSentence.setLength(currentLength);
            }
        }
    }
}

```

Approach 2: Dynamic Programming - Memoization
```java

class Solution {

    // Main function to break the string into words
    public List<String> wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        Map<String, List<String>> memoization = new HashMap<>();
        return dfs(s, wordSet, memoization);
    }

    // Depth-first search function to find all possible word break combinations
    private List<String> dfs(
        String remainingStr,
        Set<String> wordSet,
        Map<String, List<String>> memoization
    ) {
        // Check if result for this substring is already memoized
        if (memoization.containsKey(remainingStr)) {
            return memoization.get(remainingStr);
        }

        // Base case: when the string is empty, return a list containing an empty string
        if (remainingStr.isEmpty()) return Collections.singletonList("");
        List<String> results = new ArrayList<>();
        for (int i = 1; i <= remainingStr.length(); ++i) {
            String currentWord = remainingStr.substring(0, i);
            // If the current substring is a valid word
            if (wordSet.contains(currentWord)) {
                for (String nextWord : dfs(
                    remainingStr.substring(i),
                    wordSet,
                    memoization
                )) {
                    // Append current word and next word with space in between if next word exists
                    results.add(
                        currentWord + (nextWord.isEmpty() ? "" : " ") + nextWord
                    );
                }
            }
        }
        // Memoize the results for the current substring
        memoization.put(remainingStr, results);
        return results;
    }
}

```

Approach 4: Trie Optimization

```java
// Time complexity: O(n⋅2^n)
class TrieNode {

    boolean isEnd;
    TrieNode[] children;

    TrieNode() {
        isEnd = false;
        children = new TrieNode[26];
    }
}

class Trie {

    TrieNode root;

    Trie() {
        root = new TrieNode();
    }

    void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }
}

class Solution {

    public List<String> wordBreak(String s, List<String> wordDict) {
        // Build the Trie from the word dictionary
        Trie trie = new Trie();
        for (String word : wordDict) {
            trie.insert(word);
        }

        // Map to store results of subproblems
        Map<Integer, List<String>> dp = new HashMap<>();

        // Iterate from the end of the string to the beginning
        for (int startIdx = s.length(); startIdx >= 0; startIdx--) {
            // List to store valid sentences starting from startIdx
            List<String> validSentences = new ArrayList<>();

            // Initialize current node to the root of the trie
            TrieNode currentNode = trie.root;

            // Iterate from startIdx to the end of the string
            for (int endIdx = startIdx; endIdx < s.length(); endIdx++) {
                char c = s.charAt(endIdx);
                int index = c - 'a';

                // Check if the current character exists in the trie
                if (currentNode.children[index] == null) {
                    break;
                }

                // Move to the next node in the trie
                currentNode = currentNode.children[index];

                // Check if we have found a valid word
                if (currentNode.isEnd) {
                    String currentWord = s.substring(startIdx, endIdx + 1);

                    // If it's the last word, add it as a valid sentence
                    if (endIdx == s.length() - 1) {
                        validSentences.add(currentWord);
                    } else {
                        // If it's not the last word, append it to each sentence formed by the remaining substring
                        List<String> sentencesFromNextIndex = dp.get(
                            endIdx + 1
                        );
                        for (String sentence : sentencesFromNextIndex) {
                            validSentences.add(currentWord + " " + sentence);
                        }
                    }
                }
            }

            // Store the valid sentences in dp
            dp.put(startIdx, validSentences);
        }

        // Return the sentences formed from the entire string
        return dp.getOrDefault(0, new ArrayList<>());
    }
}

```
