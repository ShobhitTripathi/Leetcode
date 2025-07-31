# Leetcode 152. Maximum Product Subarray

**Difficulty:** Medium  
**Tags:** Dynamic Programming, Array  
**Link:** https://leetcode.com/problems/maximum-product-subarray/

---

## 🧾 Problem Description

Given an integer array `nums`, find a subarray that has the largest product, and return the product.

The test cases are generated so that the answer will fit in a 32-bit integer.

---

### 🧪 Examples

#### Example 1:
```
Input: nums = [2,3,-2,4]  
Output: 6  
Explanation: [2,3] has the largest product 6.
```

#### Example 2:
```
Input: nums = [-2,0,-1]  
Output: 0  
Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
```

---

### ✅ Constraints

- 1 <= nums.length <= 2 * 10⁴  
- -10 <= nums[i] <= 10  
- The product of any subarray of nums is guaranteed to fit in a 32-bit integer.

---

## 💡 Optimized Approach

### ✅ Intuition:
- The product can be affected by negative numbers.
- A negative number can turn the smallest (most negative) product into the largest.
- So, at each step, track:
  - `maxSoFar`: max product ending at current index
  - `minSoFar`: min product ending at current index

---

## ✅ Java Code (Optimized - O(n) Time, O(1) Space)

```java
public class Solution {
    public int maxProduct(int[] nums) {
        int maxProduct = nums[0];
        int maxSoFar = nums[0];
        int minSoFar = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int current = nums[i];

            // Swap if current number is negative
            if (current < 0) {
                int temp = maxSoFar;
                maxSoFar = minSoFar;
                minSoFar = temp;
            }

            // Update max and min product ending at current index
            maxSoFar = Math.max(current, maxSoFar * current);
            minSoFar = Math.min(current, minSoFar * current);

            // Update global maximum product
            maxProduct = Math.max(maxProduct, maxSoFar);
        }

        return maxProduct;
    }
}
```

---

### 🧠 Time and Space Complexity:
- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(1)`
