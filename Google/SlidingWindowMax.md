# [239. Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/)
Hard


You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position.

Return the max sliding window.

 

Example 1:
```
Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
Output: [3,3,5,5,6,7]
Explanation: 
Window position                Max
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7
```
Example 2:
```
Input: nums = [1], k = 1
Output: [1]
``` 

Constraints:
- 1 <= nums.length <= 10<sup>5</sup>
- -10<sup>4</sup> <= nums[i] <= 10<sup>4</sup>
- 1 <= k <= nums.length

## Approach
```
- Use Deque, to keep the elements
- we'll keep the elemenet in decreasing order
- while the element at the first of the queue, i.e the index of the nums, 
    if it's out of the window, keep removing the element
- while the element at the last of the queue, i.e the index, 
    if it's less than equal to new element of the nums, keep removing the element
- insert the index of new element of nums
- if wE greater than k-1, then add the first element (index of the element in nums)
    of the queue to res
```

## Solution
```java
class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] output = new int[n - k + 1];
        Deque<Integer> q = new LinkedList<>();
        int l = 0, r = 0;

        while (r < n) {
            // Remove indices of elements smaller than the current element from the back of the deque
            while (!q.isEmpty() && nums[q.getLast()] < nums[r]) {
                q.removeLast();
            }

            // Add the current element's index to the back of the deque
            q.addLast(r);

            // Remove the index at the front if it is outside the current window
            if (l > q.getFirst()) {
                q.removeFirst();
            }

            // If the window has at least k elements, record the maximum value
            if ((r + 1) >= k) {
                output[l] = nums[q.getFirst()]; // Maximum is at the front of the deque
                l++;
            }

            r++;
        }

        return output;
    }
}

```

## Complexity Analysis
```
- Time Complexity: O(n)
- Space Complexity: O(k), where k < n
```
