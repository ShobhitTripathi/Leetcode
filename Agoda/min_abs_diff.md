Given an array, find all pairs of elements whose difference is equal to the minimum absolute difference in array. 

input = [1,3,10,7,5] 
output = [[1,3], [5,7]] 
(as minimum absolute diff in any 2 elemnents of array is 2)

This is a very common interview problem (also appears as **Minimum Absolute Difference** on LeetCode).

The key observation is:

> The minimum absolute difference can **only occur between adjacent elements in sorted order**.

---

# Thought Process

### Brute Force

Compare every pair.

```text
(1,3)
(1,10)
(1,7)
...
```

Time Complexity:

[
O(N^2)
]

Too slow.

---

## Observation

Consider

```text
1 3 5 7 10
```

Suppose someone says

```text
Minimum difference is between 1 and 7.
```

Impossible.

Because

```text
1 3 5 7
```

There are numbers in between.

One of

```text
1-3
3-5
5-7
```

must have a smaller (or equal) difference.

Therefore,

> **The minimum difference always exists between adjacent elements after sorting.**

This observation reduces the problem dramatically.

---

# Algorithm

### Step 1

Sort the array.

```text
1 3 5 7 10
```

---

### Step 2

Find the minimum adjacent difference.

```text
3-1 = 2

5-3 = 2

7-5 = 2

10-7 = 3
```

Minimum

```text
2
```

---

### Step 3

Traverse again and collect all pairs whose difference equals the minimum.

Result

```text
(1,3)

(3,5)

(5,7)
```

Notice that the expected output in your prompt:

```text
[[1,3],[5,7]]
```

is actually **incorrect** because `(3,5)` also has a difference of `2`.

The correct output is:

```text
[[1,3],[3,5],[5,7]]
```

---

# Java Solution

```java
import java.util.*;

class Solution {

    static List<List<Integer>> minimumAbsDifference(int[] arr) {

        Arrays.sort(arr);

        int minDiff = Integer.MAX_VALUE;

        // Find minimum adjacent difference
        for (int i = 1; i < arr.length; i++) {
            minDiff = Math.min(minDiff, arr[i] - arr[i - 1]);
        }

        List<List<Integer>> result = new ArrayList<>();

        // Collect all pairs having minimum difference
        for (int i = 1; i < arr.length; i++) {

            if (arr[i] - arr[i - 1] == minDiff) {

                result.add(Arrays.asList(arr[i - 1], arr[i]));
            }
        }

        return result;
    }

    public static void main(String[] args) {

        int[] arr = {1,3,10,7,5};

        System.out.println(minimumAbsDifference(arr));
    }
}
```

---

# Dry Run

Input

```text
1 3 10 7 5
```

Sort

```text
1 3 5 7 10
```

### First Pass

| Pair | Difference |
| ---- | ---------- |
| 1,3  | 2          |
| 3,5  | 2          |
| 5,7  | 2          |
| 7,10 | 3          |

Minimum

```text
2
```

---

### Second Pass

Collect

```text
(1,3)

(3,5)

(5,7)
```

---

# Why only adjacent elements?

Suppose

```text
a < b < c
```

Then

```text
c-a
```

is always greater than or equal to at least one of

```text
b-a

or

c-b
```

For example,

```text
1 5 8

Difference(1,8)=7

Difference(1,5)=4

Difference(5,8)=3
```

The farther pair can never produce the minimum.

Hence checking only adjacent pairs after sorting is sufficient.

---

# Complexity

### Sorting

```text
O(N log N)
```

### Finding minimum

```text
O(N)
```

### Collecting pairs

```text
O(N)
```

Overall

```text
Time : O(N log N)

Space : O(1)
```

(ignoring the output list; otherwise `O(k)` where `k` is the number of returned pairs).

---

## Interview Explanation

> "The brute-force approach compares every pair in `O(N²)`. The key insight is that after sorting, the minimum absolute difference must occur between adjacent elements. If two elements have another element between them, then one of the adjacent gaps is guaranteed to be less than or equal to their gap. Therefore, we sort the array, compute the minimum adjacent difference in one pass, and collect all adjacent pairs having that difference in a second pass, resulting in `O(N log N)` time."
