# 843. Guess the Word

You are given an array of unique strings words where words[i] is six letters long. One word of words was chosen as a secret word.

You are also given the helper object Master. You may call Master.guess(word) where word is a six-letter-long string, and it must be from words. Master.guess(word) returns:

-1 if word is not from words, or
an integer representing the number of exact matches (value and position) of your guess to the secret word.
There is a parameter allowedGuesses for each test case where allowedGuesses is the maximum number of times you can call Master.guess(word).

For each test case, you should call Master.guess with the secret word without exceeding the maximum number of allowed guesses. You will get:

"Either you took too many guesses, or you did not find the secret word." if you called Master.guess more than allowedGuesses times or if you did not call Master.guess with the secret word, or
"You guessed the secret word correctly." if you called Master.guess with the secret word with the number of calls to Master.guess less than or equal to allowedGuesses.
The test cases are generated such that you can guess the secret word with a reasonable strategy (other than using the bruteforce method).

 Example 1:
```
Input: secret = "acckzz", words = ["acckzz","ccbazz","eiowzz","abcczz"], allowedGuesses = 10
Output: You guessed the secret word correctly.
Explanation:
master.guess("aaaaaa") returns -1, because "aaaaaa" is not in words.
master.guess("acckzz") returns 6, because "acckzz" is secret and has all 6 matches.
master.guess("ccbazz") returns 3, because "ccbazz" has 3 matches.
master.guess("eiowzz") returns 2, because "eiowzz" has 2 matches.
master.guess("abcczz") returns 4, because "abcczz" has 4 matches.
We made 5 calls to master.guess, and one of them was the secret, so we pass the test case.
```
Example 2:
```
Input: secret = "hamada", words = ["hamada","khaled"], allowedGuesses = 10
Output: You guessed the secret word correctly.
Explanation: Since there are two words, you can guess both.
```
 

Constraints:
```
1 <= words.length <= 100
words[i].length == 6
words[i] consist of lowercase English letters.
All the strings of words are unique.
secret exists in words.
10 <= allowedGuesses <= 30
```


Core Insight
```
Each guess gives you information:
how many characters (position + value) match the secret.
A good strategy is to maximize information gain per guess,
so that the remaining candidate set shrinks fast and you always finish within the allowed guesses (10–30).

❌ Why naïve approaches fail
Random guessing → may exceed allowed guesses
Trying all words sequentially → worst-case 100 guesses (not allowed)
Greedy character matching → unreliable due to positional constraint

✅ Correct Strategy (Minimax / Information Theory)
Key Idea
For a chosen guess word g, compare it with all remaining candidates.
For each possible match count k (0 to 6):
Group candidates that would return k if g were guessed
The worst-case remaining group size determines how bad this guess could be
👉 Choose the guess that minimizes the worst-case remaining candidates
This is a classic minimax strategy.
```

Algorithm Overview
```
Maintain a list of possible candidates
For each candidate word:
Simulate how it partitions remaining words by match count
Choose the word with the smallest maximum bucket
Call master.guess(word)
Filter candidates to only those consistent with the response

Repeat until match count = 6
```

Complexity Analysis
```
Let n ≤ 100, word length = 6
Per Guess
Minimax selection: O(n² * 6)
Filtering: O(n * 6)

Total : O(10 * n²) ≈ O(n²)
```
Solution
```java
class Solution {

    public void findSecretWord(String[] words, Master master) {
        List<String> candidates = new ArrayList<>(Arrays.asList(words));

        for (int attempt = 0; attempt < 10; attempt++) {

            // Pick the best word using minimax strategy
            String guess = chooseBestWord(candidates);
            int matches = master.guess(guess);

            // Found the secret
            if (matches == 6) return;

            // Filter candidates based on feedback
            List<String> next = new ArrayList<>();
            for (String w : candidates) {
                if (match(guess, w) == matches) {
                    next.add(w);
                }
            }
            candidates = next;
        }
    }

    // Minimax choice
    private String chooseBestWord(List<String> words) {
        int minWorst = Integer.MAX_VALUE;
        String best = words.get(0);

        for (String w1 : words) {
            int[] buckets = new int[7];

            for (String w2 : words) {
                int m = match(w1, w2);
                buckets[m]++;
            }

            int worst = 0;
            for (int c : buckets) {
                worst = Math.max(worst, c);
            }

            if (worst < minWorst) {
                minWorst = worst;
                best = w1;
            }
        }
        return best;
    }

    // Count matching positions
    private int match(String a, String b) {
        int cnt = 0;
        for (int i = 0; i < 6; i++) {
            if (a.charAt(i) == b.charAt(i)) cnt++;
        }
        return cnt;
    }
}


```
