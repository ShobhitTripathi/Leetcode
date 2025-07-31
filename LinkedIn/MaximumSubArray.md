# Maximum SubArray

Given an integer array nums, find the subarray with the largest sum, and return its sum.

 

Example 1:
Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
Output: 6
Explanation: The subarray [4,-1,2,1] has the largest sum 6.

Example 2:
Input: nums = [1]
Output: 1
Explanation: The subarray [1] has the largest sum 1.

Example 3:
Input: nums = [5,4,-1,7,8]
Output: 23
Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.
 
Constraints:
1 <= nums.length <= 105
-104 <= nums[i] <= 104
 
Follow up: If you have figured out the O(n) solution, try coding another solution using the divide and conquer approach, which is more subtle.


```java

class Solution {
    public int maxSubArray(int[] nums) {
        int max = Integer.MIN_VALUE, maxSoFar = 0;

        for (int num : nums) {
            maxSoFar += num;
            if (maxSoFar > max) {
                max = maxSoFar;
            }

            if (maxSoFar < 0) {
                maxSoFar = 0;
            }
        }
        return max;
    }
}


// divide and conquer
public class Solution {
    public int maxSubArray(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }

    private int helper(int[] nums, int left, int right) {
        if (left == right) return nums[left];

        int mid = left + (right - left) / 2;
        int leftSum = helper(nums, left, mid);
        int rightSum = helper(nums, mid + 1, right);
        int crossSum = cross(nums, left, mid, right);

        return Math.max(Math.max(leftSum, rightSum), crossSum);
    }

    private int cross(int[] nums, int left, int mid, int right) {
        int leftMax = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftMax = Math.max(leftMax, sum);
        }

        int rightMax = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightMax = Math.max(rightMax, sum);
        }

        return leftMax + rightMax;
    }
}


```
