# **Problem 1: Sort an array by comparing the squares of each number**

## **Brute-Force Solution (Iterative)**

### **Idea**

1. Iterate through array and compute squares.
2. Sort the squared array.

### **Code (Iterative)**

```java
int[] sortedSquaresBrute(int[] nums) {
    int n = nums.length;
    int[] squared = new int[n];

    // 1. Square each number (iterative)
    for (int i = 0; i < n; i++) {
        squared[i] = nums[i] * nums[i];
    }

    // 2. Sort array
    Arrays.sort(squared);

    return squared;
}
```

### **Time & Space**

* Time: O(n log n)
* Space: O(n)

---

## **Optimized Two-Pointer Solution (Iterative)**

### **Idea**

Because the array is sorted, compare absolute values from both ends using two pointers.

### **Steps (Iterative)**

1. `left = 0`, `right = n - 1`, `pos = n - 1`.
2. While `left <= right`:

   * Compare `abs(nums[left])` and `abs(nums[right])`.
   * Square the larger and put it at `result[pos]`.
   * Move pointers.
3. Return result.

### **Code**

```java
int[] sortedSquaresTwoPointer(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    
    int left = 0, right = n - 1, pos = n - 1;
    
    while (left <= right) {
        int leftSq = nums[left] * nums[left];
        int rightSq = nums[right] * nums[right];
        
        if (leftSq > rightSq) {
            result[pos--] = leftSq;
            left++;
        } else {
            result[pos--] = rightSq;
            right--;
        }
    }
    
    return result;
}
```

### **Time & Space**

* Time: O(n)
* Space: O(n)

---

# **Follow-Up Problem: Find the K-th Largest Element**

We now extend the problem:
**Given the squared array, find the k-th largest element.**

We provide **two iterative solutions**.

---

# **Solution A — Max Heap (Iterative)**

### **Idea**

Push all values into a max heap, pop k times.

### **Code**

```java
int kthLargestMaxHeap(int[] nums, int k) {
    PriorityQueue<Integer> maxHeap =
        new PriorityQueue<>((a, b) -> b - a);
    
    // push all elements (iterative)
    for (int num : nums) {
        maxHeap.offer(num);
    }

    // pop k times
    int val = -1;
    for (int i = 0; i < k; i++) {
        val = maxHeap.poll();
    }
    
    return val;
}
```

### **Time & Space**

* Time: O(n log n)
* Space: O(n)

---

# **Solution B — Iterative Quickselect (Optimized)**

### **Idea**

Use partitioning to iteratively narrow the search region.

### **Code**

```java
int kthLargestQuickselect(int[] nums, int k) {
    int target = nums.length - k;  // convert to kth largest index
    int left = 0, right = nums.length - 1;

    while (left <= right) {
        int pivotIndex = partition(nums, left, right);

        if (pivotIndex == target) {
            return nums[pivotIndex];
        } else if (pivotIndex < target) {
            left = pivotIndex + 1;
        } else {
            right = pivotIndex - 1;
        }
    }
    return -1; // shouldn't happen
}

int partition(int[] nums, int left, int right) {
    int pivot = nums[right];
    int i = left;

    for (int j = left; j < right; j++) {
        if (nums[j] <= pivot) {
            int temp = nums[i]; nums[i] = nums[j]; nums[j] = temp;
            i++;
        }
    }
    int temp = nums[i]; nums[i] = nums[right]; nums[right] = temp;
    return i;
}
```

### **Time & Space**

* Average Time: O(n)
* Worst Time: O(n²)
* Space: O(1)

---

# **Summary**

### **Sorted Squares**

| Method      | Approach      | Time       | Space |
| ----------- | ------------- | ---------- | ----- |
| Brute Force | Square + sort | O(n log n) | O(n)  |
| Two Pointer | Compare ends  | O(n)       | O(n)  |

### **K-th Largest**

| Method      | Time       | Space | Notes                |
| ----------- | ---------- | ----- | -------------------- |
| Max Heap    | O(n log n) | O(n)  | Simple               |
| Quickselect | O(n) avg   | O(1)  | Best for large input |

---
