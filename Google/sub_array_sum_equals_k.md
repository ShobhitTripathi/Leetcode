# 560. Subarray Sum Equals K

Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.

A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:
```
Input: nums = [1,1,1], k = 2
Output: 2
```
Example 2:
```
Input: nums = [1,2,3], k = 3
Output: 2
```
 
Constraints:
```
1 <= nums.length <= 2 * 104
-1000 <= nums[i] <= 1000
-107 <= k <= 107
```

Approach 
```
Core idea
Let:
prefixSum[i] = sum of elements from index 0 to i

A subarray (j + 1 → i) has sum k iff:
prefixSum[i] - prefixSum[j] = k
⇒ prefixSum[j] = prefixSum[i] - k

So, for every index i, we need to know:
How many times have we seen (currentSum - k) before?

That is exactly what a hash map provides.

```

Solution
```
class Solution {
    public int subarraySum(int[] nums, int k) {

        // Map to store: prefixSum -> number of times it appears
        Map<Integer, Integer> prefixCount = new HashMap<>();

        // Base case:
        // A prefix sum of 0 exists once before starting
        // This helps when a subarray from index 0 itself sums to k
        prefixCount.put(0, 1);

        int prefixSum = 0;
        int count = 0;

        // Traverse the array
        for (int num : nums) {

            // Update running prefix sum
            prefixSum += num;

            /*
             If prefixSum - k exists in map,
             it means there are subarrays ending at current index
             whose sum equals k
            */
            if (prefixCount.containsKey(prefixSum - k)) {
                count += prefixCount.get(prefixSum - k);
            }

            // Record the current prefix sum frequency
            prefixCount.put(
                prefixSum,
                prefixCount.getOrDefault(prefixSum, 0) + 1
            );
        }

        return count;
    }
}


```
