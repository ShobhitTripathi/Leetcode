# 300. Longest Increasing Subsequence

Given an integer array nums, return the length of the longest strictly increasing subsequence.

 Example 1:
```
Input: nums = [10,9,2,5,3,7,101,18]
Output: 4
Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
```
Example 2:
```
Input: nums = [0,1,0,3,2,3]
Output: 4
```
Example 3:
```
Input: nums = [7,7,7,7,7,7,7]
Output: 1
 ```

Constraints:
```
1 <= nums.length <= 2500
-104 <= nums[i] <= 104
 ```

Follow up: Can you come up with an algorithm that runs in O(n log(n)) time complexity?



```java
// O(N Log N)

class Solution {
    public int lengthOfLIS(int[] nums) {
        // 'sub' will store the increasing subsequence we build
        // but it's not necessarily the actual subsequence from nums.
        // It just helps us track the LIS length using patience sorting idea.
        List<Integer> sub = new ArrayList<>();
        
        // Start with the first element
        sub.add(nums[0]);

        // Iterate through the rest of the array
        for (int i = 1; i < nums.length; i++) {
            int num = nums[i];

            // Case 1: Current number is greater than the last element of 'sub'
            // → Extend the subsequence by adding this number
            if (num > sub.get(sub.size() - 1)) {
                sub.add(num);
            } 
            // Case 2: Current number cannot extend 'sub'
            // → Find the position where it can replace an element in 'sub'
            //    (the first element >= num)
            else {
                int j = binarySearch(sub, num);
                sub.set(j, num);  
                // Replace keeps 'sub' valid as a potential subsequence
                // and makes future extensions possible
            }
        }

        // The size of 'sub' will give us the length of LIS
        return sub.size();
    }

    // Helper function: binary search to find the index
    // where 'num' should be placed in 'sub'
    private int binarySearch(List<Integer> sub, int num) {
        int left = 0, right = sub.size() - 1;

        // Standard binary search
        while (left <= right) {
            int mid = (left + right) / 2;

            if (sub.get(mid) == num) {
                return mid;  // Exact match found
            }

            if (sub.get(mid) < num) {
                left = mid + 1;  // Search in right half
            } else {
                right = mid - 1; // Search in left half
            }
        }

        // 'left' is the position where num should be inserted
        return left;
    }
}


```
