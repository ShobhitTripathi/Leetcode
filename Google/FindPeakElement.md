# 162. Find Peak Element

A peak element is an element that is strictly greater than its neighbors.

Given a 0-indexed integer array nums, find a peak element, and return its index. If the array contains multiple peaks, return the index to any of the peaks.

You may imagine that nums[-1] = nums[n] = -∞. In other words, an element is always considered to be strictly greater than a neighbor that is outside the array.

You must write an algorithm that runs in O(log n) time.

Example 1:
```
Input: nums = [1,2,3,1]
Output: 2
Explanation: 3 is a peak element and your function should return the index number 2.
```
Example 2:
```
Input: nums = [1,2,1,3,5,6,4]
Output: 5
Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
```
Constraints:
```
1 <= nums.length <= 1000
-231 <= nums[i] <= 231 - 1
nums[i] != nums[i + 1] for all valid i.
```

Approach
```
We use binary search by comparing the mid element with its neighbor to decide which side must contain a peak,
shrinking the search space until one element remains.

Core Invariant (Important Interview Point):
At every step:
We discard the half that cannot contain a peak
Because a peak always exists in any array (boundary elements count as peaks)

Why This Works (Intuition):
If the array is descending at mid, a peak lies to the left
If the array is ascending at mid, a peak lies to the right
We exploit this monotonic behavior locally, not globally
This allows binary search without full sorting.

Edge Cases Covered Automatically:
Single element array
Strictly increasing array → last element is peak
Strictly decreasing array → first element is peak
Multiple peaks → returns any one peak (allowed by problem)

```

```java
// O (log n)
class Solution {

    public int findPeakElement(int[] nums) {

        // Binary search boundaries
        int l = 0;
        int r = nums.length - 1;

        // Continue until search space reduces to one element
        while (l < r) {

            // Mid index
            int mid = (l + r) / 2;

            /*
             * Compare mid with its right neighbor.
             *
             * Case 1: nums[mid] > nums[mid + 1]
             * There is a peak on the left side (including mid),
             * because the slope is descending.
             *
             * Case 2: nums[mid] < nums[mid + 1]
             * There is a peak on the right side,
             * because the slope is ascending.
             */
            if (nums[mid] > nums[mid + 1]) {
                r = mid;        // peak lies in [l, mid]
            } else {
                l = mid + 1;    // peak lies in [mid + 1, r]
            }
        }

        // When l == r, it must be a peak
        return l;
    }
}


```
