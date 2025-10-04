# Split Array Largest Sum

Given an integer array nums and an integer k, split nums into k non-empty subarrays such that the largest sum of any subarray is minimized.
Return the minimized largest sum of the split.
A subarray is a contiguous part of the array.

Example 1:
```
Input: nums = [7,2,5,10,8], k = 2
Output: 18
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [7,2,5] and [10,8], where the largest sum among the two subarrays is only 18.
```
Example 2:
```
Input: nums = [1,2,3,4,5], k = 2
Output: 9
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [1,2,3] and [4,5], where the largest sum among the two subarrays is only 9.
```
 

Constraints:
```
1 <= nums.length <= 1000
0 <= nums[i] <= 106
1 <= k <= min(50, nums.length)
```

Approaches Overview
```
Approach 3:Binary Search + Greedy (Optimal)

Intuition:

  The minimum possible largest sum lies between:
  max(nums) (can’t be smaller than the largest element)
  and sum(nums) (if all in one subarray).

  Use binary search on this range:
    For a guessed value X,
    check if we can split the array into ≤ k subarrays such that each subarray sum ≤ X.
    If yes → try smaller X
    If no → try larger X

Algorithm (Binary Search + Greedy)
Compute:
  maxElement = max(nums)
  sum = sum(nums)
  Set search range → left = maxElement, right = sum

  While left <= right:
    mid = (left + right) / 2
    Compute subarraysRequired(nums, mid):
    Traverse array, greedily group elements until sum > mid
    Increment count and reset current sum
    If subarrays ≤ k → possible answer → move left
    Else → move right
Return smallest valid mid.
```


Solution
```java
class Solution {

    private int minimumSubarraysRequired(int[] nums, int maxSumAllowed) {
        int currentSum = 0;
        int splitsRequired = 0;

        for (int element : nums) {
            // Add element only if the sum doesn't exceed maxSumAllowed 
            if (currentSum + element <= maxSumAllowed) {
                currentSum += element;
            } else {
                // If the element addition makes sum more than maxSumAllowed
                // Increment the splits required and reset sum
                currentSum = element;
                splitsRequired++;
            }
        }

        // Return the number of subarrays, which is the number of splits + 1
        return splitsRequired + 1;
    }


    public int splitArray(int[] nums, int k) {
        // Find the sum of all elements and the maximum element
        int sum = Arrays.stream(nums).sum();
        int maxElement = Arrays.stream(nums).max().getAsInt();

        // Define the left and the right coundary of binary Search
        int left = maxElement;
        int right = sum;
        int minimumLargestSplitSum = 0;

        while (left <= right) {
            // find the mid value
            int maxSumAllowed = left + (right - left) / 2;

            // Find the minimum splits. Idf the splitsRequired is less than 
            // or equal to k, move towards left i.e., smaller values
            if (minimumSubarraysRequired(nums, maxSumAllowed) <= k) {
                right = maxSumAllowed - 1;
                minimumLargestSplitSum = maxSumAllowed;
            } else {
                // Move towards right if splitsRequired is more than k
                left = maxSumAllowed + 1;
            }
        }

        return minimumLargestSplitSum;
    }
}   

```


Other approaches
```
Approach 1:
  Top-Down Dynamic Programming
  Recursively decide where to split.
  Memoize results for overlapping subproblems.
  Time: O(N² * K)
  Space: O(N * K) (memo + recursion stack)

Approach 2:
  Bottom-Up DP
  Iteratively build DP table using prefix sums.
  Time: O(N² * K)
  Space: O(N * K)
```
