
# 📞 Phone Number to Words

## ❓ Problem Description

Given a list of words (`dictionary`) and a phone number (`digits`), return all words that can be formed by the digits based on the T9 phone keypad mapping.

### T9 Mapping

```
2 → a, b, c  
3 → d, e, f  
4 → g, h, i  
5 → j, k, l  
6 → m, n, o  
7 → p, q, r, s  
8 → t, u, v  
9 → w, x, y, z  
```

## ✅ Example

```
Input:
dictionary = ["tree", "used", "apple"]
phoneNumber = "8733"

Output:
["tree", "used"]
```

## 🔍 Explanation

- "tree" → "8733"
- "used" → "8733"
- Both match the phone number.

## 💡 Approach

1. Build a map of characters to digits.
2. For each word in the dictionary, convert it to its corresponding digit string.
3. Compare with the given phone number.
4. Collect all matching words.

## ✅ Java Solution

```java
import java.util.*;

public class PhoneNumberToWords {
    private static final Map<Character, Character> t9Map = new HashMap<>();

    static {
        t9Map.put('a', '2'); t9Map.put('b', '2'); t9Map.put('c', '2');
        t9Map.put('d', '3'); t9Map.put('e', '3'); t9Map.put('f', '3');
        t9Map.put('g', '4'); t9Map.put('h', '4'); t9Map.put('i', '4');
        t9Map.put('j', '5'); t9Map.put('k', '5'); t9Map.put('l', '5');
        t9Map.put('m', '6'); t9Map.put('n', '6'); t9Map.put('o', '6');
        t9Map.put('p', '7'); t9Map.put('q', '7'); t9Map.put('r', '7'); t9Map.put('s', '7');
        t9Map.put('t', '8'); t9Map.put('u', '8'); t9Map.put('v', '8');
        t9Map.put('w', '9'); t9Map.put('x', '9'); t9Map.put('y', '9'); t9Map.put('z', '9');
    }

    public static List<String> getMatchingWords(List<String> dictionary, String phoneNumber) {
        List<String> result = new ArrayList<>();
        for (String word : dictionary) {
            if (word.length() != phoneNumber.length()) continue;
            StringBuilder mapped = new StringBuilder();
            for (char c : word.toLowerCase().toCharArray()) {
                if (!t9Map.containsKey(c)) {
                    mapped = null;
                    break;
                }
                mapped.append(t9Map.get(c));
            }
            if (mapped != null && mapped.toString().equals(phoneNumber)) {
                result.add(word);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> dictionary = Arrays.asList("tree", "used", "apple");
        String phoneNumber = "8733";
        System.out.println(getMatchingWords(dictionary, phoneNumber)); // Output: [tree, used]
    }
}
```

## 🧠 Complexity

- Time: O(N * L), where N is the number of words, and L is the average word length.
- Space: O(1), excluding output list.
