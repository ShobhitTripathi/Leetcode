# 📞 Phone Keypad Word Matcher

## 🧩 Problem Statement

Given the standard mapping from English letters to digits on a phone keypad:

```
1 → ""
2 → a, b, c  
3 → d, e, f  
4 → g, h, i  
5 → j, k, l  
6 → m, n, o  
7 → p, q, r, s  
8 → t, u, v  
9 → w, x, y, z
```

You are given a list of known valid words and a numeric phone number string. You need to output all words from the known list that match the phone number when letters in the word are mapped to corresponding digits.

### Input

- `KNOWN_WORDS` = `["careers", "linkedin", "hiring", "interview", "linkedgo"]`
- `phoneNumber`: A string of digits (2–9) representing a phone number.

### Output

- Return all words from `KNOWN_WORDS` whose digit encoding matches the given phone number.

### Examples

```
Input: phoneNumber = "2273377"
Output: ["careers"]

Input: phoneNumber = "54653346"
Output: ["linkedin", "linkedgo"]
```

---

## ✅ Solution: Java Code

```java
import java.util.*;

public class PhoneNumberWordMapper {
    // Digit to characters mapping
    private static final Map<Character, Character> letterToDigitMap = new HashMap<>();

    static {
        String[] mapping = {
            "",     // 0
            "",     // 1
            "abc",  // 2
            "def",  // 3
            "ghi",  // 4
            "jkl",  // 5
            "mno",  // 6
            "pqrs", // 7
            "tuv",  // 8
            "wxyz"  // 9
        };

        for (int digit = 2; digit <= 9; digit++) {
            for (char ch : mapping[digit].toCharArray()) {
                letterToDigitMap.put(ch, (char) (digit + '0'));
            }
        }
    }

    // Convert a word to digit representation
    private static String wordToDigits(String word) {
        StringBuilder sb = new StringBuilder();
        for (char ch : word.toLowerCase().toCharArray()) {
            sb.append(letterToDigitMap.getOrDefault(ch, ' '));
        }
        return sb.toString();
    }

    // Main function to match words
    public static List<String> getMatchingWords(String phoneNumber, List<String> knownWords) {
        Map<String, List<String>> digitToWordsMap = new HashMap<>();

        // Build digit to words map
        for (String word : knownWords) {
            String digits = wordToDigits(word);
            digitToWordsMap.computeIfAbsent(digits, k -> new ArrayList<>()).add(word);
        }

        // Lookup phone number
        return digitToWordsMap.getOrDefault(phoneNumber, new ArrayList<>());
    }

    // Driver
    public static void main(String[] args) {
        List<String> knownWords = Arrays.asList("careers", "linkedin", "hiring", "interview", "linkedgo");

        System.out.println(getMatchingWords("2273377", knownWords));   // [careers]
        System.out.println(getMatchingWords("54653346", knownWords));  // [linkedin, linkedgo]
        System.out.println(getMatchingWords("447464", knownWords));    // []
    }
}
```

---

## 🧠 Time and Space Complexity

- **Time Complexity:** O(k * m), where `k` is the number of known words and `m` is the average length of a word.
- **Space Complexity:** O(k), for storing the digit-to-word map.

---
