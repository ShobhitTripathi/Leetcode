This is LeetCode 1138 – Alphabet Board Path (Medium).

The optimal approach is Greedy + Coordinate Mapping. 
We don't need BFS because every character has a fixed position, and we can move directly from the current character to the target character.

## 1. Key observation

The board is a 5-column grid:

```
a b c d e
f g h i j
k l m n o
p q r s t
u v w x y
z
```

Each character has coordinates:

- Row = `(ch - 'a') / 5`
- Column = `(ch - 'a') % 5`

For every character in `target`:

1. Calculate its row and column.
2. Calculate the difference from the current position.
3. Generate the required moves.
4. Append `!` to select the character.
5. Update the current position.

Important edge case: `z` is alone in the last row. We must move down to `z` only after completing horizontal movements, and move up from `z` before horizontal movements.

## 2. Java solution (optimal)

```

class Solution {
    public String alphabetBoardPath(String target) {

        StringBuilder result = new StringBuilder();

        int currRow = 0;
        int currCol = 0;

        for (char ch : target.toCharArray()) {

            int targetRow = (ch - 'a') / 5;
            int targetCol = (ch - 'a') % 5;

            // Special case: Move UP first when leaving z.
            while (currRow > targetRow) {
                result.append('U');
                currRow--;
            }

            // Move LEFT before moving vertically into z.
            while (currCol > targetCol) {
                result.append('L');
                currCol--;
            }

            // Move RIGHT.
            while (currCol < targetCol) {
                result.append('R');
                currCol++;
            }

            // Move DOWN only after horizontal movement.
            while (currRow < targetRow) {
                result.append('D');
                currRow++;
            }

            // Select current character.
            result.append('!');
        }

        return result.toString();
    }
}

```

## 3. Why this ordering?

The critical case is `z`.

Consider moving from `a` to `z`.

- `a = (0,0)`
- `z = (5,0)`

We need:

```
DDDDD!
```

This is valid.

Now consider moving from `z` to `u`.

- `z = (5,0)`
- `u = (4,0)`

We need:

```
U!
```

More importantly, consider moving from `z` to `y`:

- `z = (5,0)`
- `y = (4,4)`

Correct path:

```
URRRR!
```

If we move right first, we would attempt to move to an invalid position in row 5.

Therefore:

- Move UP first.
- Move LEFT next.
- Move RIGHT next.
- Move DOWN last.

This ordering guarantees that every intermediate position is valid.

## 4. Complexity analysis

Let `n` be the length of `target`.

| Complexity | Value | Reason                         |
| ---------- | ----- | ------------------------------ |
| Time       | O(n)  | At most 10 moves per character |
| Space      | O(n)  | Result string                  |

The board is fixed, so the number of moves per character is bounded by a constant.

## 5. Interview cheat sheet

Remember these three things:

1. Coordinate mapping: `row = (ch - 'a') / 5`, `col = (ch - 'a') % 5`.
2. Greedy movement: Move directly towards each target character.
3. Handle z: UP before horizontal movement; DOWN after horizontal movement.

Interview takeaway: This is not a shortest-path/BFS problem. Since the board is a regular grid and every move has equal cost, Manhattan distance gives the minimum number of moves. The only complication is ensuring that the path never enters an invalid cell around `z`.
