# Array Rank Transform

## Problem Statement

Given an array of integers `arr`, replace each element with its **rank**.

Rules:

* Rank starts from **1**.
* Larger element ⇒ Larger rank.
* Equal elements must have the **same rank**.
* Rank should be **as small as possible**.

### Example

```text
Input:
arr = [40,10,20,30]

Output:
[4,1,2,3]
```

```text
Input:
arr = [100,100,50]

Output:
[2,2,1]
```

---

# Solution 1: Sort + HashMap (Most Common)

## Idea

* Create a sorted copy of the array.
* Assign ranks only to unique elements.
* Store the mapping in a `HashMap`.
* Replace each original element using the map.

## Java

```java
class Solution {

    public int[] arrayRankTransform(int[] arr) {

        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        Map<Integer, Integer> rank = new HashMap<>();

        int currentRank = 1;

        // Assign rank to unique values.
        for (int num : sorted) {
            if (!rank.containsKey(num)) {
                rank.put(num, currentRank++);
            }
        }

        // Replace each element with its rank.
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rank.get(arr[i]);
        }

        return arr;
    }
}
```

### Complexity

| Metric | Complexity     |
| ------ | -------------- |
| Time   | **O(N log N)** |
| Space  | **O(N)**       |

---

# Solution 2: TreeSet + HashMap

## Idea

* `TreeSet` automatically removes duplicates **and keeps elements sorted**.
* Traverse the TreeSet to assign ranks.
* Store rank in a HashMap.
* Replace elements using the map.

## Java

```java
class Solution {

    public int[] arrayRankTransform(int[] arr) {

        HashMap<Integer, Integer> numToRank = new HashMap<>();

        TreeSet<Integer> nums = new TreeSet<>();

        // TreeSet keeps unique values in sorted order.
        for (int num : arr) {
            nums.add(num);
        }

        int rank = 1;

        for (int num : nums) {
            numToRank.put(num, rank++);
        }

        // Replace each element with its rank.
        for (int i = 0; i < arr.length; i++) {
            arr[i] = numToRank.get(arr[i]);
        }

        return arr;
    }
}
```

### Complexity

| Metric | Complexity     |
| ------ | -------------- |
| Time   | **O(N log N)** |
| Space  | **O(N)**       |

### Notes

* No need to create a copy of the array.
* `TreeSet` handles sorting and duplicate removal together.
* Slightly cleaner but internally still uses a balanced BST.

---

# Solution 3: TreeMap

## Idea

* Use a `TreeMap` where:

  * **Key = element**
  * **Value = list of indices where it appears**
* TreeMap keeps keys sorted automatically.
* Traverse keys in sorted order and assign ranks directly to original indices.

## Java

```java
class Solution {

    public int[] arrayRankTransform(int[] arr) {

        TreeMap<Integer, List<Integer>> numToIndices = new TreeMap<>();

        // Store all indices for each value.
        for (int i = 0; i < arr.length; i++) {

            if (!numToIndices.containsKey(arr[i])) {
                numToIndices.put(arr[i], new ArrayList<>());
            }

            numToIndices.get(arr[i]).add(i);
        }

        int rank = 1;

        // Keys are already sorted.
        for (int num : numToIndices.keySet()) {

            for (int index : numToIndices.get(num)) {
                arr[index] = rank;
            }

            rank++;
        }

        return arr;
    }
}
```

### Complexity

| Metric | Complexity     |
| ------ | -------------- |
| Time   | **O(N log N)** |
| Space  | **O(N)**       |

### Notes

* No separate HashMap is required.
* Directly updates the original array.
* Stores indices, so memory usage is slightly higher than the previous approaches.

---

# Comparison

| Approach              | Main Idea                                     | Time           | Space    |
| --------------------- | --------------------------------------------- | -------------- | -------- |
| **Sort + HashMap**    | Sort copy → Map value to rank → Replace       | **O(N log N)** | **O(N)** |
| **TreeSet + HashMap** | TreeSet gives sorted unique values → Map rank | **O(N log N)** | **O(N)** |
| **TreeMap**           | TreeMap stores sorted keys and indices        | **O(N log N)** | **O(N)** |

---

# Which Solution to Prefer?

| Situation                                               | Recommended Solution                                      |
| ------------------------------------------------------- | --------------------------------------------------------- |
| Coding Interviews                                       | ✅ **Sort + HashMap** (most common and easiest to explain) |
| Want automatic sorting + deduplication                  | ✅ **TreeSet + HashMap**                                   |
| Need to keep track of original indices while processing | ✅ **TreeMap**                                             |

---

# Key Takeaways

* The **rank depends only on the sorted order of unique elements**.
* Duplicates always receive the **same rank**.
* All three approaches have the same asymptotic complexity:

  * **Time:** `O(N log N)`
  * **Space:** `O(N)`
* The difference lies in **how the sorted unique elements are obtained**:

  * **Sort + HashMap:** Sort a copied array.
  * **TreeSet:** Automatically maintains unique sorted values.
  * **TreeMap:** Automatically maintains sorted keys while storing additional information (indices).
