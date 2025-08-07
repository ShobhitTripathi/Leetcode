# 🧩 Make Largest Special - Explanation

## 🔍 Problem Statement

A **special binary string** is a binary string with the following two properties:

1. It has the same number of `1`s and `0`s.
2. Every prefix of the string has **at least as many** `1`s as `0`s.

Given a **special binary string** `S`, the goal is to **return the lexicographically largest** special binary string that can be obtained by swapping **consecutive non-overlapping** special substrings of `S`.

---

## ✅ Example

**Input:** `"11011000"`  
**Output:** `"11100100"`

**Explanation:**
- The input can be split into special substrings: `"11011000"` → `"11011000"`  
- Inside: `"11011000"` → `"110"` and `"11000"` → split further  
- Re-arrange `"110"` and `"11000"` → `"11100100"` (which is larger lexicographically)

---

## 💡 Approach (Recursive + Greedy)

1. **Divide** the string into special substrings (similar to parsing balanced parentheses).
2. **Recursively** sort each special substring into its lexicographically largest form.
3. **Sort all the substrings** in descending lex order.
4. **Concatenate** and return the result.

---

## 🔎 Java Code with Comments

```java
class Solution {
    public String makeLargestSpecial(String S) {
        // Base case: empty string
        if (S.length() == 0) return S;

        int anchor = 0; // start index of a special substring
        int bal = 0; // balance counter for number of 1s and 0s
        List<String> mountains = new ArrayList(); // list of special substrings

        // Loop through each character to find balanced substrings
        for (int i = 0; i < S.length(); ++i) {
            // Increment or decrement balance based on character
            bal += S.charAt(i) == '1' ? 1 : -1;

            // When balance is zero, we found a special substring
            if (bal == 0) {
                // Recursively solve the inner part and wrap with '1' and '0'
                String inner = makeLargestSpecial(S.substring(anchor + 1, i));
                mountains.add("1" + inner + "0");

                // Move anchor to the next character
                anchor = i + 1;
            }
        }

        // Sort substrings in descending order to form the largest lex string
        Collections.sort(mountains, Collections.reverseOrder());

        // Build final result by concatenating sorted special substrings
        StringBuilder ans = new StringBuilder();
        for (String mtn : mountains)
            ans.append(mtn);

        return ans.toString();
    }
}
```

---

## 🧠 Time & Space Complexity

- **Time Complexity:**  
  - Each level of recursion processes `n` characters and breaks it into smaller parts.  
  - Sorting adds a log factor → **O(n log n)** overall.

- **Space Complexity:**  
  - **O(n)** for recursion stack and result building.

---

## 🔁 Recap of Key Concepts

- Divide-and-conquer over special substrings.
- Use **balance counting** (like for valid parentheses).
- Greedy: sort parts in **descending** order for maximum lexicographic value.