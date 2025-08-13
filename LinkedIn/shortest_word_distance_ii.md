
# LeetCode 244: Shortest Word Distance II

## 🧠 Problem Statement

Design a class `WordDistance` with the following capabilities:

1. **Constructor:**  
   Accepts an array `wordsDict[]` of strings and preprocesses it.

2. **Method `shortest(word1, word2)`:**  
   Returns the minimum distance (absolute index difference) between two distinct words `word1` and `word2` in the original `wordsDict`.

You can assume `word1 != word2` and both words exist in the array.

### Example
```
Input:
words = ["practice", "makes", "perfect", "coding", "makes"]

WordDistance obj = new WordDistance(words);
obj.shortest("coding", "practice") -> 3
obj.shortest("makes", "coding")   -> 1
```

---

## ✅ Solution Outline

### Preprocessing (Constructor)
- Traverse `wordsDict`.
- Build a hash map (`Map<String, List<Integer>>`) mapping each word to a of its indices in the array.
- Time: O(N), Space: O(N), where N is the total number of words.

### Query (`shortest(word1, word2)`)
- Retrieve both index lists.
- Use a **two-pointer technique**:
  - Compute `abs(list1[i] − list2[j])` and update minimum.
  - Advance the pointer that points to the smaller index.
- Time: O(L₁ + L₂), where L₁ and L₂ are lengths of the two index lists.

---

## 💻 Code Snippets

### Java
```java
class WordDistance {
    // Map each word to a list of its indices in the words array
    private Map<String, List<Integer>> map;

    public WordDistance(String[] words) {
        map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            // Store all positions (indices) where each word appears
            map.computeIfAbsent(words[i], k -> new ArrayList<>()).add(i);
        }
    }

    public int shortest(String w1, String w2) {
        // Get the index lists for both words
        List<Integer> l1 = map.get(w1), l2 = map.get(w2);
        int i = 0, j = 0, min = Integer.MAX_VALUE;

        // Use two pointers to find the minimum distance between occurrences
        while (i < l1.size() && j < l2.size()) {
            int idx1 = l1.get(i), idx2 = l2.get(j);
            min = Math.min(min, Math.abs(idx1 - idx2)); // update min distance

            // Move the pointer pointing to the smaller index forward
            if (idx1 < idx2) i++;
            else j++;
        }
        return min; // return the smallest distance found
    }
}

```


---

## 🔍 Time & Space Complexity

| Operation             | Time Complexity       | Space Complexity |
|----------------------|-----------------------|------------------|
| **Constructor**       | O(N)                  | O(N)             |
| **shortest(query)**   | O(L₁ + L₂) per query  | O(1) extra       |

---

## 💡 Key Insight

Preprocessing with a hash map and two-pointer logic during query gives optimal performance when many queries follow one input list.
