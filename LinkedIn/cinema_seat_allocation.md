# 🎬 Cinema Seat Allocation

## Problem Description

A cinema has `n` rows, and each row contains 10 seats labeled from 1 to 10.

We need to allocate seats to families of 4 members such that:
- All 4 members sit **together in the same row**.
- They must occupy **4 consecutive seats**.
- Each row can accommodate at most **two families** if there is no overlap.

Valid blocks of 4 seats per row (non-overlapping):
- Block A: Seats 2, 3, 4, 5
- Block B: Seats 4, 5, 6, 7
- Block C: Seats 6, 7, 8, 9

> Note: Block B overlaps with A and C. So a maximum of **two** families can be seated in a row if A and C are both available.

### Input

- `n` — total number of rows in the cinema (1 ≤ n ≤ 10^9)
- `reservedSeats` — a list of reserved seats as a list of `[row, seat]` pairs

### Output

Return the **maximum number of 4-person families** you can seat in the cinema.

---

## Example

### Input:
```
n = 3  
reservedSeats = [[1,2], [1,3], [1,8], [2,6], [3,1], [3,10]]
```

### Output:
```
4
```

### Explanation:
- Row 1: block A (2-5) is blocked; block C (6-9) is available → 1 family  
- Row 2: block B (4-7) is blocked → no family  
- Row 3: no reserved seat blocks any of A/C → 2 families  
- Total: 1 + 0 + 2 + (3-3)*2 = 3 families

---

## 🧠 Optimized Java Solution

```java
import java.util.*;

public class Solution {
    public int maxNumberOfFamilies(int n, int[][] reservedSeats) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int[] seat : reservedSeats) {
            int row = seat[0], col = seat[1];
            // Use 10-bit mask to mark reserved seats in row
            if (col >= 2 && col <= 9) {
                map.put(row, map.getOrDefault(row, 0) | (1 << col));
            }
        }

        int result = (n - map.size()) * 2; // Rows with no reservations can fit 2 families

        for (int row : map.keySet()) {
            int bitmask = map.get(row);

            boolean left = (bitmask & (1 << 2 | 1 << 3 | 1 << 4 | 1 << 5)) == 0;
            boolean middle = (bitmask & (1 << 4 | 1 << 5 | 1 << 6 | 1 << 7)) == 0;
            boolean right = (bitmask & (1 << 6 | 1 << 7 | 1 << 8 | 1 << 9)) == 0;

            if (left && right) {
                result += 2;
            } else if (left || middle || right) {
                result += 1;
            }
        }

        return result;
    }
}
```

---

## 🧮 Time and Space Complexity

- **Time:** O(k), where `k` is the number of reserved seats.
- **Space:** O(m), where `m` is the number of unique rows with reservations.

---

## ✅ Key Takeaways

- Rows without any reserved seats always fit 2 families.
- Bitmasking optimizes space and speeds up checks.
- Only need to process rows that have reserved seats.
