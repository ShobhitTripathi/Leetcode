Problem Statement
```
 2598. Smallest Missing Non-negative Integer After Operations

You are given a 0-indexed integer array nums and an integer value.

In one operation, you can add or subtract value from any element of nums.

For example, if nums = [1,2,3] and value = 2, you can choose to subtract value from nums[0] to make nums = [-1,2,3].
The MEX (minimum excluded) of an array is the smallest missing non-negative integer in it.

For example, the MEX of [-1,2,3] is 0 while the MEX of [1,0,3] is 2.
Return the maximum MEX of nums after applying the mentioned operation any number of times.

 

Example 1:

Input: nums = [1,-10,7,13,6,8], value = 5
Output: 4
Explanation: One can achieve this result by applying the following operations:
- Add value to nums[1] twice to make nums = [1,0,7,13,6,8]
- Subtract value from nums[2] once to make nums = [1,0,2,13,6,8]
- Subtract value from nums[3] twice to make nums = [1,0,2,3,6,8]
The MEX of nums is 4. It can be shown that 4 is the maximum MEX we can achieve.
Example 2:

Input: nums = [1,-10,7,13,6,8], value = 7
Output: 2
Explanation: One can achieve this result by applying the following operation:
- subtract value from nums[2] once to make nums = [1,-10,0,13,6,8]
The MEX of nums is 2. It can be shown that 2 is the maximum MEX we can achieve.

```

Approach
```
Approach: Modulo + Frequency Array (Greedy)
1. Group elements by remainder: Since we can add or subtract value, an element's remainder (num % value) never changes. So, we group numbers based on their remainders.
2. Handle negative numbers: Normalize the remainder using ((num % value) + value) % value to ensure it is always non-negative.
3. Count frequencies: Use an array of size value to store how many elements belong to each remainder group.
4. Find MEX: Start from 0 and check whether an element is available in its corresponding remainder group (mex % value). If available, consume one occurrence and increment mex.
5. Return the answer: Stop when the required remainder group has no available elements. The current mex is the smallest missing non-negative integer.
Complexity:
- Time: O(n)
- Space: O(value)
```

Solution
```java
// O(n), O(value)
class Solution {

    public int findSmallestInteger(int[] nums, int value) {
        int[] mp = new int[value];
        for (int x : nums) {
            int v = ((x % value) + value) % value;
            mp[v]++;
        }
        int mex = 0;
        while (mp[mex % value] > 0) {
            mp[mex % value]--;
            mex++;
        }
        return mex;
    }
}
```
