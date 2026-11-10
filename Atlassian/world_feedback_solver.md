This problem has two parts:

1. Wordle Feedback Generation: Compare the guessed word with the secret word and generate `G`, `Y`, or `X`.
2. Wordle Solver: Use the feedback to eliminate impossible words and iteratively find the secret word within `max_attempts`.

I'll explain the approach and provide an optimized Java implementation.

## 1. Part 1: Generate Feedback

The tricky part is handling duplicate characters correctly.

For example:

```
Guess:  SPEED
Secret: APPLE
```

A letter can only be marked Yellow if an unmatched occurrence exists in the secret word.

### Algorithm

We need two passes:

Pass 1: Identify Green characters

- If `guess[i] == secret[i]`, mark `G`.
- Otherwise, count the unmatched characters in the secret word.

Pass 2: Identify Yellow characters

\- For every position not already marked Green:

- If the guessed character exists in the remaining character frequency map, mark `Y` and decrement its frequency.
- Otherwise, mark `X`.

### Java Solution

```

public String generateFeedback(String guess, String secret) {
    int n = guess.length();

    char[] result = new char[n];
    int[] freq = new int[26];

    // First pass: Mark Green and count unmatched secret characters
    for (int i = 0; i < n; i++) {
        if (guess.charAt(i) == secret.charAt(i)) {
            result[i] = 'G';
        } else {
            freq[secret.charAt(i) - 'a']++;
        }
    }

    // Second pass: Mark Yellow or Gray
    for (int i = 0; i < n; i++) {
        if (result[i] == 'G') {
            continue;
        }

        int index = guess.charAt(i) - 'a';

        if (freq[index] > 0) {
            result[i] = 'Y';
            freq[index]--;
        } else {
            result[i] = 'X';
        }
    }

    return new String(result);
}

```

Complexity:

- Time: O(5) = O(1)
- Space: O(26) = O(1)

## 2. Part 2: Build the Wordle Solver

Now we build a solver that finds the secret word.

### Key observation

We don't need to maintain complicated game state.

We only need to:

1. Pick the first word from `potentialSecrets`.
2. Generate feedback for that guess.
3. If feedback is `GGGGG`, we found the secret.
4. Otherwise, eliminate every word that would not produce the same feedback.
5. Repeat.

The important concept is that every remaining candidate must produce exactly the same feedback against the previous guess.

### Example walkthrough

Suppose:

```
potentialSecrets = ["crane", "plane", "slate", "flame"]
secret = "plane"
maxAttempts = 5
```

Attempt 1

# crane

XXGGG

Compare `crane` against `plane`.

Remaining candidates:

- plane
- slate
- flame

Attempt 2

# plane

GGGGG

Secret found

Note: The first feedback is `XXGGG`, so only candidates producing that same feedback against `crane` survive.

## 3. Complete Java Implementation

We'll create a `WordleSolver` class with two methods:

- `generateFeedback()` – generates feedback.
- `solve()` – iteratively narrows down the candidate list.

```

import java.util.*;

public class WordleSolver {

    // Generate Wordle feedback
    public String generateFeedback(String guess, String secret) {
        int[] freq = new int[26];
        char[] result = new char[5];

        // First pass: Mark Green and count unmatched letters
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == secret.charAt(i)) {
                result[i] = 'G';
            } else {
                freq[secret.charAt(i) - 'a']++;
            }
        }

        // Second pass: Mark Yellow or Gray
        for (int i = 0; i < 5; i++) {
            if (result[i] == 'G') {
                continue;
            }

            int index = guess.charAt(i) - 'a';

            if (freq[index] > 0) {
                result[i] = 'Y';
                freq[index]--;
            } else {
                result[i] = 'X';
            }
        }

        return new String(result);
    }

    // Solve the Wordle game
    public String solve(List<String> potentialSecrets,
                        String secret,
                        int maxAttempts) {

        List<String> candidates = new ArrayList<>(potentialSecrets);

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            if (candidates.isEmpty()) {
                break;
            }

            // Always pick the first candidate
            String guess = candidates.get(0);

            // Generate feedback
            String feedback = generateFeedback(guess, secret);

            System.out.println(
                "Attempt " + attempt +
                ": Guess = " + guess +
                ", Feedback = " + feedback
            );

            // Check if secret is found
            if (feedback.equals("GGGGG")) {
                return guess;
            }

            // Filter candidates based on feedback
            List<String> remaining = new ArrayList<>();

            for (String candidate : candidates) {

                String candidateFeedback =
                    generateFeedback(guess, candidate);

                if (candidateFeedback.equals(feedback)) {
                    remaining.add(candidate);
                }
            }

            candidates = remaining;
        }

        return null; // Secret not found within maxAttempts
    }

    public static void main(String[] args) {

        WordleSolver solver = new WordleSolver();

        List<String> words = Arrays.asList(
            "crane", "plane", "slate", "flame"
        );

        String result = solver.solve(words, "plane", 5);

        System.out.println("Result: " + result);
    }
}

```

## 4. Complexity Analysis

Let:

- `N` = number of potential secrets.
- `A` = maximum attempts.
- `L` = word length (5).

| Operation         | Complexity   |
| ----------------- | ------------ |
| Generate feedback | O(L)         |
| Filter candidates | O(N × L)     |
| Total solver      | O(A × N × L) |
| Auxiliary space   | O(N + 26)    |

Since the word length is fixed at 5:

- Time: O(A × N)
- Space: O(N)

## 5. Important Interview Discussion Points

There are three things an interviewer may explore further:

1. Duplicate letters: Why do we need two passes instead of checking whether a character exists in the secret word?
2. Candidate filtering: Why compare feedback against every remaining candidate instead of simply checking Green and Yellow positions?
3. Optimization: Can we precompute feedback patterns or use bitsets to make filtering faster for a very large dictionary?

One important distinction: This implementation assumes the actual secret is available to the solver for simulation. In a real Wordle game, the solver would receive feedback from the game after each guess, rather than receiving the secret word directly.

The core algorithmic pattern to remember is:

Guess → Feedback → Filter candidates → Repeat.
